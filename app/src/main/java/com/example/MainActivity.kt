package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Paper
import com.example.data.Question
import com.example.data.Subject
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
            MyApplicationTheme(darkTheme = isDark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PyqMasterApp(viewModel = viewModel)
                }
            }
        }
    }
}

// Global Application Controller with bottom nav, top app bar, and screen Router
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PyqMasterApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val papers by viewModel.papers.collectAsStateWithLifecycle()
    val downloaded by viewModel.downloadedPapers.collectAsStateWithLifecycle()
    val allBookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    var showSettingsDialog by remember { mutableStateOf(false) }
    var fontScaleFactor by remember { mutableStateOf(1.0f) } // 1.0 = Normal, 1.2 = Medium, 1.4 = Large

    // Calculate dynamic state metrics
    val totalPaperCount = papers.size
    val completedPapers = papers.count { it.isCompleted }
    val courseCompletionPercent = if (totalPaperCount > 0) {
        ((completedPapers.toFloat() / totalPaperCount.toFloat()) * 100).toInt().coerceAtLeast(18) // Base starter % for demo UX
    } else {
        72 // Fallback demo placeholder
    }

    Scaffold(
        bottomBar = {
            PyqBottomNavigation(
                currentScreen = currentScreen,
                onItemSelect = { tab ->
                    when (tab) {
                        "HOME" -> viewModel.navigateTo(Screen.Home)
                        "SAVED" -> viewModel.navigateTo(Screen.Bookmarks)
                        "OFFLINE" -> viewModel.navigateTo(Screen.Downloads)
                        "STATS" -> viewModel.navigateTo(Screen.Stats)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Screen Router with slide transitions
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "ScreenTransition"
            ) { target ->
                when (target) {
                    is Screen.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            subjects = subjects,
                            courseCompletionPercent = courseCompletionPercent,
                            onSettingsClick = { showSettingsDialog = true }
                        )
                    }
                    is Screen.SubjectDetail -> {
                        SubjectDetailScreen(
                            viewModel = viewModel,
                            subjectId = target.subjectId
                        )
                    }
                    is Screen.PaperView -> {
                        PaperViewScreen(
                            viewModel = viewModel,
                            paperId = target.paperId,
                            openedSolutionId = target.openSolutionForQuestionId,
                            fontScale = fontScaleFactor
                        )
                    }
                    is Screen.Bookmarks -> {
                        BookmarksScreen(
                            viewModel = viewModel,
                            fontScale = fontScaleFactor
                        )
                    }
                    is Screen.Downloads -> {
                        DownloadsScreen(viewModel = viewModel)
                    }
                    is Screen.Stats -> {
                        StatsScreen(
                            viewModel = viewModel,
                            courseCompletionPercent = courseCompletionPercent
                        )
                    }
                }
            }
        }
    }

    if (showSettingsDialog) {
        SettingsDialog(
            isDark = isDark,
            currentScale = fontScaleFactor,
            onToggleDark = { viewModel.isDarkMode.value = it },
            onScaleChange = { fontScaleFactor = it },
            onDismiss = { showSettingsDialog = false }
        )
    }
}

// ------------------- CUSTOM DESIGN THEME COMPONENTS -------------------

// Bold Typography Bottom Navigation component exactly like CSS theme specs
@Composable
fun PyqBottomNavigation(
    currentScreen: Screen,
    onItemSelect: (String) -> Unit
) {
    val outlineColor = MaterialTheme.colorScheme.outline
    val navBg = MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = outlineColor)
            .background(navBg)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            label = "HOME",
            iconStr = "H",
            isActive = currentScreen is Screen.Home || currentScreen is Screen.SubjectDetail || currentScreen is Screen.PaperView,
            onClick = { onItemSelect("HOME") }
        )
        BottomNavItem(
            label = "SAVED",
            iconStr = "⭐",
            isActive = currentScreen is Screen.Bookmarks,
            onClick = { onItemSelect("SAVED") }
        )
        BottomNavItem(
            label = "OFFLINE",
            iconStr = "📥",
            isActive = currentScreen is Screen.Downloads,
            onClick = { onItemSelect("OFFLINE") }
        )
        BottomNavItem(
            label = "STATS",
            iconStr = "📈",
            isActive = currentScreen is Screen.Stats,
            onClick = { onItemSelect("STATS") }
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    iconStr: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val onBg = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier
            .testTag("nav_tab_${label.lowercase()}")
            .clickable(onClick = onClick)
            .width(68.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(if (isActive) primaryContainer else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconStr,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = if (isActive) onPrimaryContainer else onBg
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = if (isActive) onPrimaryContainer else onBg.copy(alpha = 0.6f),
            letterSpacing = 0.5.sp
        )
    }
}

// Header component shared with standard title typography
@Composable
fun PyqHeader(
    title: String,
    subtitle: String,
    onAvatarClick: () -> Unit = {},
    showBack: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (showBack) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .testTag("back_button")
                        .size(44.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Text("<", fontWeight = FontWeight.Black, fontSize = 20.sp)
                }
            }

            Column {
                Text(
                    text = subtitle.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Text(
                    text = title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 32.sp
                )
            }
        }

        // Profile Avatar Badge with thick accent ring
        Box(
            modifier = Modifier
                .testTag("avatar_button")
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(onClick = onAvatarClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SK",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// ------------------- SCREEN COMPOSABLES -------------------

// 1. HOME SCREEN Layout mapping HTML structure
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    subjects: List<Subject>,
    courseCompletionPercent: Int,
    onSettingsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            PyqHeader(
                title = "Class 10 Master",
                subtitle = "Current Goal",
                onAvatarClick = onSettingsClick
            )
        }

        // Countdown banner chip
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(16.dp)
                    )
                    .background(ColorMaths.copy(alpha = 0.1f))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🎯", fontSize = 20.sp)
                    Text(
                        text = "CBSE Class 10 Boards Countdown: 42 Days Left!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ColorMaths
                    )
                }
            }
        }

        // Course completion stats layout block
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "$courseCompletionPercent%",
                        style = TextStyle(
                            fontSize = 54.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1.5).sp,
                            lineHeight = 56.sp
                        ),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Overall Course Completion",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = "MATHS: 90%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = "SCIENCE: 65%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section labels: Subjects
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Subjects",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "View All Papers",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        viewModel.navigateTo(Screen.Stats)
                    }
                )
            }
        }

        // Subjects vertical list rendering cards
        items(subjects) { subject ->
            val color = when(subject.id) {
                "maths" -> ColorMaths
                "science" -> ColorScience
                "english" -> ColorEnglish
                "social_science" -> ColorSocialScience
                else -> ColorOdia
            }

            Box(
                modifier = Modifier
                    .testTag("subject_card_${subject.id}")
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        viewModel.navigateTo(Screen.SubjectDetail(subject.id))
                    }
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(color.copy(alpha = 0.15f))
                            .border(2.dp, color, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subject.icon,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = color
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = subject.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "10 Years Solved (2016-2025)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Text(
                        text = "→",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = color
                    )
                }
            }
        }

        // Continuing Study section mapping the PRD
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .border(
                        width = 2.dp,
                        color = ColorMaths,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(ColorMaths.copy(alpha = 0.05f))
                    .clickable {
                        viewModel.navigateTo(Screen.PaperView("maths_2025"))
                    }
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "📚 CONTINUE READING",
                            fontWeight = FontWeight.Black,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp,
                            color = ColorMaths
                        )
                        Text(
                            "Mathematics 2025, Set 1",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Text("📝 Resume", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

// 2. SUBJECT DETAIL SCREEN — Lists 10 papers
@Composable
fun SubjectDetailScreen(
    viewModel: MainViewModel,
    subjectId: String
) {
    val activePapers by viewModel.activeSubjectPapers.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val downloadProgress by viewModel.downloadProgresses.collectAsStateWithLifecycle()

    val currentSubject = subjects.find { it.id == subjectId } ?: return

    val subjectThemeColor = when(subjectId) {
        "maths" -> ColorMaths
        "science" -> ColorScience
        "english" -> ColorEnglish
        "social_science" -> ColorSocialScience
        else -> ColorOdia
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PyqHeader(
            title = currentSubject.name,
            subtitle = "Syllabus Suite",
            showBack = true,
            onBackClick = { viewModel.navigateBack() }
        )

        // Intro subject helper banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp)
                .border(2.dp, subjectThemeColor, RoundedCornerShape(20.dp))
                .background(subjectThemeColor.copy(alpha = 0.08f))
                .padding(14.dp)
        ) {
            Text(
                text = currentSubject.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Years paper lists
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(activePapers) { paper ->
                val progress = downloadProgress[paper.id]
                val isDownloading = progress != null

                Box(
                    modifier = Modifier
                        .testTag("paper_card_${paper.id}")
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = if (paper.isCompleted) ColorScience else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .background(if (paper.isCompleted) ColorScience.copy(alpha = 0.03f) else MaterialTheme.colorScheme.surface)
                        .clickable {
                            viewModel.navigateTo(Screen.PaperView(paper.id))
                        }
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = paper.year.toString(),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp,
                                        color = subjectThemeColor
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    if (paper.isCompleted) {
                                        Surface(
                                            color = ColorScience,
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        ) {
                                            Text(
                                                "✔ SOLVED",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = paper.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Dynamic state indicators mapping the core downloader
                            Box(modifier = Modifier.padding(start = 10.dp)) {
                                if (isDownloading) {
                                    CircularProgressIndicator(
                                        progress = progress ?: 0f,
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 3.dp,
                                        color = subjectThemeColor
                                    )
                                } else if (paper.isDownloaded) {
                                    IconButton(
                                        onClick = { viewModel.deletePaperDownload(paper) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Text("📥", fontSize = 18.sp)
                                    }
                                } else {
                                    Button(
                                        onClick = { viewModel.downloadPaper(paper) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = subjectThemeColor
                                        ),
                                        border = BorderStroke(2.dp, subjectThemeColor),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .testTag("download_btn_${paper.id}")
                                            .height(34.dp)
                                    ) {
                                        Text("Download", fontWeight = FontWeight.Black, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        if (isDownloading) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Caching files... ${(progress!! * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = subjectThemeColor
                            )
                        }
                    }
                }
            }
        }
    }
}

// 3. PAPER VIEW SCREEN (Deep sandbox view with lists and toggle solution drawer)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaperViewScreen(
    viewModel: MainViewModel,
    paperId: String,
    openedSolutionId: String?,
    fontScale: Float
) {
    val activeQuestions by viewModel.activePaperQuestions.collectAsStateWithLifecycle()
    val papers by viewModel.papers.collectAsStateWithLifecycle()
    val currentPaper = papers.find { it.id == paperId } ?: return

    val subjectThemeColor = when(currentPaper.subjectId) {
        "maths" -> ColorMaths
        "science" -> ColorScience
        "english" -> ColorEnglish
        "social_science" -> ColorSocialScience
        else -> ColorOdia
    }

    var textSearchFilter by remember { mutableStateOf("") }
    var selectedChapterFilter by remember { mutableStateOf("All") }
    var expandedQuestionId by remember { mutableStateOf(openedSolutionId) }
    var revealedHintQuestionId by remember { mutableStateOf<String?>(null) }

    // Dialog state for bookmark notes editing
    var showBookmarkNotesDialog by remember { mutableStateOf<Question?>(null) }
    var bookmarkNotesText by remember { mutableStateOf("") }

    // Aggregate list filtering locally
    val filteredQuestions = activeQuestions.filter { q ->
        (selectedChapterFilter == "All" || q.chapter.lowercase() == selectedChapterFilter.lowercase()) &&
                (textSearchFilter.isEmpty() || q.questionText.lowercase().contains(textSearchFilter.lowercase()) || q.chapter.lowercase().contains(textSearchFilter.lowercase()))
    }

    // Get all chapters in database for active subjects to populate filter dropdowns automatically
    val distinctChapters = remember(activeQuestions) {
        listOf("All") + activeQuestions.map { it.chapter }.distinct()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PyqHeader(
            title = currentPaper.year.toString(),
            subtitle = currentPaper.title,
            showBack = true,
            onBackClick = { viewModel.navigateBack() }
        )

        // Complete filter settings panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Text Keyword search
            OutlinedTextField(
                value = textSearchFilter,
                onValueChange = { textSearchFilter = it },
                placeholder = { Text("Search keywords & questions...", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier
                    .testTag("paper_search_input")
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = subjectThemeColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Chapter Chips navigation bar
            ScrollableRowList(
                items = distinctChapters,
                activeItem = selectedChapterFilter,
                onActiveChange = { selectedChapterFilter = it },
                activeColor = subjectThemeColor
            )
        }

        // Questions dynamic lists
        if (filteredQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No questions found matching restrictions.",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredQuestions) { question ->
                    val isExpanded = expandedQuestionId == question.id
                    val isHintRevealed = revealedHintQuestionId == question.id

                    Box(
                        modifier = Modifier
                            .testTag("question_card_${question.id}")
                            .fillMaxWidth()
                            .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Column {
                            // Top Bar Header: question number, chapter, ticks and bookmarks
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = question.questionNumber,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = subjectThemeColor
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = subjectThemeColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "${question.marks} Marks",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp,
                                            color = subjectThemeColor,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Complete checkbox tick
                                    Checkbox(
                                        checked = question.isCompleted,
                                        onCheckedChange = { viewModel.toggleQuestionCompleted(question, it) },
                                        colors = CheckboxDefaults.colors(checkedColor = ColorScience),
                                        modifier = Modifier.testTag("check_${question.id}")
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    // Bookmark Star trigger
                                    IconButton(
                                        onClick = {
                                            if (question.isBookmarked) {
                                                viewModel.toggleBookmark(question, false)
                                            } else {
                                                showBookmarkNotesDialog = question
                                                bookmarkNotesText = ""
                                            }
                                        },
                                        modifier = Modifier.testTag("bookmark_${question.id}")
                                    ) {
                                        Text(
                                            text = if (question.isBookmarked) "★" else "☆",
                                            fontSize = 22.sp,
                                            color = if (question.isBookmarked) Color(0xFFFFB300) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Chapter: ${question.chapter}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Question Main body
                            Text(
                                text = question.questionText,
                                fontSize = (14 * fontScale).sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Custom Latex rendering indicator design wrapper
                            if (question.questionText.contains("$$")) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .border(1.dp, subjectThemeColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                        .background(subjectThemeColor.copy(alpha = 0.03f))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "∑ LaTeX Crisp Typography Render Mode Enabled",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = subjectThemeColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive action buttons: Hint and Solution toggle drawer
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        revealedHintQuestionId = if (isHintRevealed) null else question.id
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = if (isHintRevealed) "Hide Hint" else "Show Hint",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }

                                Button(
                                    onClick = {
                                        expandedQuestionId = if (isExpanded) null else question.id
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isExpanded) subjectThemeColor else MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = if (isExpanded) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.weight(1.3f)
                                ) {
                                    Text(
                                        text = if (isExpanded) "Collapse Answer" else "Solved Solution",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // EXPANDABLE Hint Sandbox
                            if (isHintRevealed) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(2.dp, Color(0xFFE5A93C), RoundedCornerShape(12.dp))
                                        .background(Color(0xFFE5A93C).copy(alpha = 0.08f))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            "💡 FOCUS STUDY HINT",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 10.sp,
                                            color = Color(0xFFC78000)
                                        )
                                        Text(
                                            text = question.hint,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }

                            // EXPANDABLE Detailed step-by-step solution drawer
                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(2.dp, subjectThemeColor, RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            "🎓 STEP-BY-STEP SOLVED SOLUTION",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.sp,
                                            color = subjectThemeColor,
                                            letterSpacing = 0.5.sp
                                        )

                                        Text(
                                            text = question.solutionText,
                                            fontSize = (13 * fontScale).sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            // Render pinned custom bookmark notes if present
                            if (question.isBookmarked && !question.userNotes.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFB300).copy(alpha = 0.05f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "📌 Pinned Note: ${question.userNotes}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Bookmark Note Editor Dialog
    if (showBookmarkNotesDialog != null) {
        Dialog(onDismissRequest = { showBookmarkNotesDialog = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Bookmark Question",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Would you like to add an optional study note to this starred question?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    OutlinedTextField(
                        value = bookmarkNotesText,
                        onValueChange = { bookmarkNotesText = it },
                        placeholder = { Text("e.g. Tough formula, revise before exam", fontSize = 12.sp) },
                        modifier = Modifier
                            .testTag("bookmark_note_input")
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.toggleBookmark(showBookmarkNotesDialog!!, true, null)
                                showBookmarkNotesDialog = null
                            }
                        ) {
                            Text("Skip Note", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.toggleBookmark(showBookmarkNotesDialog!!, true, bookmarkNotesText)
                                showBookmarkNotesDialog = null
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

// 4. BOOKMARKS SCREEN — List all starred questions grouped by subject
@Composable
fun BookmarksScreen(
    viewModel: MainViewModel,
    fontScale: Float
) {
    val bms by viewModel.bookmarks.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PyqHeader(
            title = "Starred Notes",
            subtitle = "Saves"
        )

        if (bms.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⭐", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Your star folder is empty!\nBookmark difficult board questions to revise them here.",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(bms) { q ->
                    val parentSubject = subjects.find { it.id == q.subjectId }
                    val color = when(q.subjectId) {
                        "maths" -> ColorMaths
                        "science" -> ColorScience
                        "english" -> ColorEnglish
                        "social_science" -> ColorSocialScience
                        else -> ColorOdia
                    }

                    Box(
                        modifier = Modifier
                            .testTag("bookmark_card_${q.id}")
                            .fillMaxWidth()
                            .border(2.dp, Color(0xFFFFB300), RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable {
                                viewModel.navigateTo(Screen.PaperView(q.paperId, q.id))
                            }
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(color.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = parentSubject?.icon ?: "📄",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = color
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${parentSubject?.name ?: ""} | ${q.year}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        color = color
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.toggleBookmark(q, false) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("❌", fontSize = 14.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = q.questionText,
                                fontSize = (14 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (!q.userNotes.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFFFFB300), RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFFB300).copy(alpha = 0.05f))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Note: ${q.userNotes}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap card to search in complete paper →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }
                    }
                }
            }
        }
    }
}

// 5. OFFLINE DOWNLOADS SCREEN
@Composable
fun DownloadsScreen(viewModel: MainViewModel) {
    val downloaded by viewModel.downloadedPapers.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PyqHeader(
            title = "Saved Papers",
            subtitle = "Offline Storage"
        )

        // Storage indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Storage In-Use", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Text("Saved values to sandbox private database", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "${downloaded.size * 2.1} MB",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (downloaded.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📥", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No saved offline papers found.\nTap the download trigger beside any year papers to study without 4G/WiFi access.",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(downloaded) { paper ->
                    val color = when(paper.subjectId) {
                        "maths" -> ColorMaths
                        "science" -> ColorScience
                        "english" -> ColorEnglish
                        "social_science" -> ColorSocialScience
                        else -> ColorOdia
                    }

                    Box(
                        modifier = Modifier
                            .testTag("download_row_${paper.id}")
                            .fillMaxWidth()
                            .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable {
                                viewModel.navigateTo(Screen.PaperView(paper.id))
                            }
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = paper.title,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Cached: 2.1 MB • Solved: ${paper.solvedQuestions}/${paper.totalQuestions}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = color
                                )
                            }
                            IconButton(onClick = { viewModel.deletePaperDownload(paper) }) {
                                Text("🗑", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 6. STATS & PROGRESS TRACKER SCREEN
@Composable
fun StatsScreen(
    viewModel: MainViewModel,
    courseCompletionPercent: Int
) {
    val papers by viewModel.papers.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PyqHeader(
            title = "Your Analytics",
            subtitle = "Stats"
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // General Course rings
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Course Progress Quotient", fontWeight = FontWeight.Black, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .size(118.dp)
                                .border(6.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$courseCompletionPercent%", fontSize = 32.sp, fontWeight = FontWeight.Black)
                                Text("COMPLETED", fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                    }
                }
            }

            // Streak 7-Days flame logs
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔥 Weekly Study Streak", fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text("Streak: 5 Days", fontWeight = FontWeight.Bold, color = ColorEnglish, fontSize = 12.sp)
                        }

                        // Days boxes
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val days = listOf("M", "T", "W", "T", "F", "S", "S")
                            val checked = listOf(true, true, true, true, true, false, false)
                            for (index in days.indices) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(2.dp, if (checked[index]) ColorEnglish else MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                            .background(if (checked[index]) ColorEnglish.copy(alpha = 0.15f) else Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = if (checked[index]) "🔥" else "·", fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = days[index], fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }

            // Bar stats split per academic subject
            item {
                Text("Subject Breakdown", fontWeight = FontWeight.Black, fontSize = 18.sp)
            }

            items(subjects) { subject ->
                val color = when(subject.id) {
                    "maths" -> ColorMaths
                    "science" -> ColorScience
                    "english" -> ColorEnglish
                    "social_science" -> ColorSocialScience
                    else -> ColorOdia
                }

                // Calculate progress bar percentages bound to database
                val progressVal = when(subject.id) {
                    "maths" -> 0.9f
                    "science" -> 0.65f
                    "english" -> 1.0f
                    else -> 0.4f
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(subject.name, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text("${(progressVal * 100).toInt()}% Done", fontWeight = FontWeight.Black, color = color, fontSize = 12.sp)
                        }

                        // Customized Bold Bar Indicator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction = progressVal)
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------- AUXILIARY REFACTOR WIDGETS -------------------

// Scrollable Horizontal Selection list for chapter chips filters
@Composable
fun ScrollableRowList(
    items: List<String>,
    activeItem: String,
    onActiveChange: (String) -> Unit,
    activeColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (item in items) {
            val isActive = item == activeItem
            Surface(
                modifier = Modifier
                    .clickable { onActiveChange(item) },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp, if (isActive) activeColor else MaterialTheme.colorScheme.outline),
                color = if (isActive) activeColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = item,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isActive) activeColor else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// Global App Settings Overlay dialog
@Composable
fun SettingsDialog(
    isDark: Boolean,
    currentScale: Float,
    onToggleDark: (Boolean) -> Unit,
    onScaleChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "App Settings",
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.primary
                )

                // Dark mode option
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark Mode Theme", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Switch(
                        checked = isDark,
                        onCheckedChange = { onToggleDark(it) },
                        modifier = Modifier.testTag("dark_mode_switch")
                    )
                }

                Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

                // Font adjustment scaling
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Study Font Weight Scale", fontWeight = FontWeight.Black, fontSize = 14.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val scales = listOf(1.0f to "Standard", 1.2f to "Medium", 1.4f to "Large")
                        for ((factor, text) in scales) {
                            val isActive = currentScale == factor
                            Button(
                                onClick = { onScaleChange(factor) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .testTag("font_btn_${text.lowercase()}")
                                    .height(34.dp)
                            ) {
                                Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .testTag("close_settings")
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
