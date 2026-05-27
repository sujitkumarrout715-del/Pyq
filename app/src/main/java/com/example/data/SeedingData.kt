package com.example.data

object SeedingData {
    fun getInitialSubjects(): List<Subject> {
        return listOf(
            Subject(
                id = "maths",
                name = "Mathematics",
                icon = "∑",
                colorHex = "0xFF005FB0",
                description = "Class 10 CBSE Maths - Algebra, Trigonometry, Geometry, Statistics & Probability."
            ),
            Subject(
                id = "science",
                name = "Science",
                icon = "⚛",
                colorHex = "0xFF00854A",
                description = "Physics (Optics, Electricity), Chemistry (Reactions, carbon), Biology (Life Processes)."
            ),
            Subject(
                id = "english",
                name = "English Litt.",
                icon = "Abc",
                colorHex = "0xFF8D5000",
                description = "First Flight, Footprints Without Feet, Creative Writing & Advanced Grammar Section."
            ),
            Subject(
                id = "social_science",
                name = "Social Science",
                icon = "🌍",
                colorHex = "0xFF743CB4",
                description = "History (Nationalism), Geography (Resources), Civic Fed, & Economic Development."
            ),
            Subject(
                id = "odia",
                name = "Odia Language",
                icon = "📝",
                colorHex = "0xFFB51A1A",
                description = "Sahitya Deepti, Grammar (Krudanta, Taddhita, Samasa) and Solved Essays."
            )
        )
    }

    fun getInitialPapers(): List<Paper> {
        val papers = mutableListOf<Paper>()
        val subjects = listOf("maths", "science", "english", "social_science", "odia")
        val years = (2016..2025).toList()

        for (subj in subjects) {
            val titleSubj = when(subj) {
                "maths" -> "Mathematics"
                "science" -> "Science"
                "english" -> "English Language & Literature"
                "social_science" -> "Social Science"
                else -> "Odia Language"
            }
            for (yr in years) {
                papers.add(
                    Paper(
                        id = "${subj}_$yr",
                        subjectId = subj,
                        year = yr,
                        title = "CBSE Class 10 $titleSubj - $yr (Set 1 Solved)",
                        isDownloaded = yr == 2025 || yr == 2024, // Prefill a couple as downloaded for amazing UX!
                        downloadProgress = if (yr == 2025 || yr == 2024) 1.0f else 0.0f,
                        isCompleted = false,
                        totalQuestions = if (subj == "maths" || subj == "science") 5 else 3,
                        solvedQuestions = 0
                    )
                )
            }
        }
        return papers
    }

    fun getInitialQuestions(): List<Question> {
        val questions = mutableListOf<Question>()

        // ================= MATHS QUESTIONS =================
        // Maths 2025 - Q1 (Quadratic Equations)
        questions.add(
            Question(
                id = "maths_2025_q1",
                paperId = "maths_2025",
                subjectId = "maths",
                year = 2025,
                chapter = "Quadratic Equations",
                questionNumber = "Section A, Q1",
                marks = 2,
                questionText = "Find the roots of the quadratic equation: \n\n\$\$ 2x^2 - 5x + 3 = 0 \$\$\n\nby the factorization (splitting the middle term) method.",
                hint = "Think of two numbers whose sum is -5 and product is 2 * 3 = 6.",
                solutionText = "### Step-by-Step Solution:\n\n" +
                        "**Step 1:** Compare the given equation \$2x^2 - 5x + 3 = 0\$ with the standard form \$ax^2 + bx + c = 0\$.\n" +
                        "Here, \$a = 2\$, \$b = -5\$, \$c = 3\$.\n\n" +
                        "**Step 2:** Find the split factors. We need two numbers whose:\n" +
                        "- **Sum** = \$b = -5\$\n" +
                        "- **Product** = \$a \\times c = 2 \\times 3 = 6\$\n" +
                        "The two numbers are **-2** and **-3**.\n\n" +
                        "**Step 3:** Split the middle term:\n" +
                        "\$\$ 2x^2 - 2x - 3x + 3 = 0 \$\$\n\n" +
                        "**Step 4:** Group terms and factor out the common elements:\n" +
                        "\"\$\$ 2x(x - 1) - 3(x - 1) = 0 \$\$\"\n" +
                        "Take \$(x-1)\$ common:\n" +
                        "\$\$ (2x - 3)(x - 1) = 0 \$\$\n\n" +
                        "**Step 5:** Equate each factor to zero to find the roots:\n" +
                        "1. Either \$2x - 3 = 0 \\implies 2x = 3 \\implies x = \\frac{3}{2}\$\n" +
                        "2. Or \$x - 1 = 0 \\implies x = 1\$\n\n" +
                        "**Final Answer:** The roots of the quadratic equation are **\$x = \\frac{3}{2}\$** and **\$x = 1\$**."
            )
        )

        // Maths 2024 - Q1 (Arithmetic Progressions)
        questions.add(
            Question(
                id = "maths_2024_q1",
                paperId = "maths_2024",
                subjectId = "maths",
                year = 2024,
                chapter = "Arithmetic Progressions",
                questionNumber = "Section B, Q6",
                marks = 3,
                questionText = "Determine the 10th term of the Arithmetic Progression (AP):\n\n**\$\$ 2, 7, 12, 17, ... \$\$**",
                hint = "Use the formula for the nth term of an AP: \$a_n = a + (n - 1)d\$.",
                solutionText = "### Step-by-Step Solution:\n\n" +
                        "**Step 1:** Identify the parameter values from the given AP series:\n" +
                        "- First term (\$a\$) = **\$2\$**\n" +
                        "- Common difference (\$d\$) = \$T_2 - T_1 = 7 - 2 = \$ **\$5\$**\n" +
                        "- Term index (\$n\$) = **\$10\$**\n\n" +
                        "**Step 2:** Recall the nth term formula:\n" +
                        "\$\$ a_n = a + (n - 1)d \$\$\n\n" +
                        "**Step 3:** Substitute the values of \$a\$, \$d\$, and \$n\$ into the formula:\n" +
                        "\$\$ a_{10} = 2 + (10 - 1) \\times 5 \$\$\n" +
                        "\$\$ a_{10} = 2 + 9 \\times 5 \$\$\n" +
                        "\$\$ a_{10} = 2 + 45 \$\$\n" +
                        "\$\$ a_{10} = 47 \$\$\n\n" +
                        "**Final Answer:** The 10th term of the given AP is **\$47\$**."
            )
        )

        // Maths 2023 - Q1 (Trigonometry)
        questions.add(
            Question(
                id = "maths_2023_q1",
                paperId = "maths_2023",
                subjectId = "maths",
                year = 2023,
                chapter = "Trigonometry",
                questionNumber = "Section C, Q15",
                marks = 3,
                questionText = "If \$3 \\cot \\theta = 4\$, evaluate the value of:\n\n\$\$ \\frac{5 \\sin \\theta - 3 \\cos \\theta}{5 \\sin \\theta + 3 \\cos \\theta} \$\$",
                hint = "Convert the expression by dividing both numerator and denominator by \$\\sin \\theta\$.",
                solutionText = "### Step-by-Step Solution:\n\n" +
                        "**Step 1:** Simplify the given term:\n" +
                        "\$\$ 3 \\cot \\theta = 4 \\implies \\cot \\theta = \\frac{4}{3} \$\$\n\n" +
                        "**Step 2:** Observe the target expression:\n" +
                        "\$\$ E = \\frac{5 \\sin \\theta - 3 \\cos \\theta}{5 \\sin \\theta + 3 \\cos \\theta} \$\$\n\n" +
                        "**Step 3:** Divide every term in the numerator and denominator by \$\\sin \\theta\$ (Alternative to finding Sine and Cosine via triangle theorem):\n" +
                        "\$\$ E = \\frac{5\\frac{\\sin \\theta}{\\sin \\theta} - 3\\frac{\\cos \\theta}{\\sin \\theta}}{5\\frac{\\sin \\theta}{\\sin \\theta} + 3\\frac{\\cos \\theta}{\\sin \\theta}} \$\$\n\n" +
                        "Since \$\\frac{\\cos \\theta}{\\sin \\theta} = \\cot \\theta\$, we get:\n" +
                        "\$\$ E = \\frac{5 - 3 \\cot \\theta}{5 + 3 \\cot \\theta} \$\$\n\n" +
                        "**Step 4:** Substitute \$\\cot \\theta = \\frac{4}{3}\$ into the equation:\n" +
                        "\$\$ E = \\frac{5 - 3\\left(\\frac{4}{3}\\right)}{5 + 3\\left(\\frac{4}{3}\\right)} \$\$\n" +
                        "\$\$ E = \\frac{5 - 4}{5 + 4} \$\$\n" +
                        "\$\$ E = \\frac{1}{9} \$\$\n\n" +
                        "**Final Answer:** The evaluated value of the expression is **\$\\frac{1}{9}\$**."
            )
        )

        // Maths 2022 - Q1 (Geometry - Coordinate Geometry)
        questions.add(
            Question(
                id = "maths_2022_q1",
                paperId = "maths_2022",
                subjectId = "maths",
                year = 2022,
                chapter = "Coordinate Geometry",
                questionNumber = "Section B, Q5",
                marks = 2,
                questionText = "Find the coordinates of the point which divides the line segment joining the points \$A(4, -3)\$ and \$B(9, 7)\$ internally in the ratio \$3:2\$.",
                hint = "Use the Section Formula: \$P(x, y) = \\left(\\frac{m_1x_2 + m_2x_1}{m_1+m_2}, \\frac{m_1y_2 + m_2y_1}{m_1+m_2}\\right)\$.",
                solutionText = "### Step-by-Step Solution:\n\n" +
                        "**Step 1:** Note the inputs:\n" +
                        "- Point \$A(x_1, y_1) = (4, -3)\$\n" +
                        "- Point \$B(x_2, y_2) = (9, 7)\$\n" +
                        "- Ratio \$m_1 : m_2 = 3 : 2\$\n\n" +
                        "**Step 2:** Apply Section Formula for coordinates \$(x, y)\$:\n" +
                        "\$\$ x = \\frac{m_1 x_2 + m_2 x_1}{m_1 + m_2} \$\$\n" +
                        "\$\$ y = \\frac{m_1 y_2 + m_2 y_1}{m_1 + m_2} \$\$\n\n" +
                        "**Step 3:** Calculate \$x\$:\n" +
                        "\$\$ x = \\frac{3 \\times 9 + 2 \\times 4}{3 + 2} = \\frac{27 + 8}{5} = \\frac{35}{5} = 7 \$\$\n\n" +
                        "**Step 4:** Calculate \$y\$:\n" +
                        "\$\$ y = \\frac{3 \\times 7 + 2 \\times (-3)}{3 + 2} = \\frac{21 - 6}{5} = \\frac{15}{5} = 3 \$\$\n\n" +
                        "**Final Answer:** The dividing coordinates are **\$(7, 3)\$**."
            )
        )


        // ================= SCIENCE QUESTIONS =================
        // Science 2025 - Q1 (Optics - Refraction)
        questions.add(
            Question(
                id = "science_2025_q1",
                paperId = "science_2025",
                subjectId = "science",
                year = 2025,
                chapter = "Light - Reflection & Refraction",
                questionNumber = "Section A, Q4",
                marks = 3,
                questionText = "The refractive index of medium A is 1.5, and that of medium B is 2.0. \n\n" +
                        "1. In which medium does light travel faster? \n" +
                        "2. Find the ratio of the speed of light in medium A to medium B.",
                hint = "Refractive Index (\$n\$) is inversely proportional to velocity (\$v\$): \$n = \\frac{c}{v}\$.",
                solutionText = "### Step-by-Step Solution:\n\n" +
                        "**Part 1: Identifying the faster medium**\n" +
                        "The absolute refractive index (\$n\$) is defined as:\n" +
                        "\$\$ n = \\frac{c}{v} \\implies v = \\frac{c}{n} \$\$\n" +
                        "This indicates that the **velocity of light (\$v\$) is inversely proportional** to the refractive index.\n" +
                        "- Medium A has a lower refractive index (\$n_A = 1.5\$).\n" +
                        "- Medium B has a higher refractive index (\$n_B = 2.0\$).\n\n" +
                        "Therefore, light travels **faster** in **Medium A**.\n\n" +
                        "**Part 2: Ratio of Speed**\n" +
                        "Let \$v_A\$ and \$v_B\$ be the speeds of light in Medium A and B respectively:\n" +
                        "\$\$ v_A = \\frac{c}{n_A} \\quad \\text{and} \\quad v_B = \\frac{c}{n_B} \$\$\n\n" +
                        "Taking the ratio of speed \$v_A\$ to speed \$v_B\$:\n" +
                        "\$\$ \\frac{v_A}{v_B} = \\frac{c/n_A}{c/n_B} = \\frac{n_B}{n_A} \$\$\n" +
                        "Substitute the values:\n" +
                        "\$\$ \\frac{v_A}{v_B} = \\frac{2.0}{1.5} = \\frac{4}{3} \$\$\n\n" +
                        "**Final Answer:**\n" +
                        "1. Light travels faster in **Medium A**.\n" +
                        "2. The ratio of speed of light in A to B is **\$4 : 3\$**."
            )
        )

        // Science 2024 - Q1 (Chemistry - Acids and Bases)
        questions.add(
            Question(
                id = "science_2024_q1",
                paperId = "science_2024",
                subjectId = "science",
                year = 2024,
                chapter = "Acids, Bases, and Salts",
                questionNumber = "Section B, Q8",
                marks = 2,
                questionText = "Why does an aqueous solution of an acid conduct electricity, whereas a solution of glucose or alcohol does not?",
                hint = "Electrical current conduction requires free ions that act as charge carriers.",
                solutionText = "### Step-by-Step Solution:\n\n" +
                        "**Step 1:** Explain acidic behavior in water:\n" +
                        "When an acid (e.g., \$HCl\$) is dissolved in water, it dissociates/ionizes to release hydrogen ions (\$H^+\$ or hydronium \$H_3O^+\$):\n" +
                        "\$\$ HCl + H_2O \\longrightarrow H_3O^+ + Cl^- \$\$\n" +
                        "These mobile charged ions act as charge carriers to complete the circuit and conduct electricity.\n\n" +
                        "**Step 2:** Explain the glucose/alcohol case:\n" +
                        "Compounds like glucose (\$C_6H_{12}O_6\$) and alcohol (\$C_2H_5OH\$) do not dissociate or ionize in aqueous solution. They remain as whole molecules. \n" +
                        "Because there are no free ions available to carry currents, they are non-conductors.\n\n" +
                        "**Conclusion:** The presence of **free \$H^+\$ ions** in the aqueous acid solution allows electricity to flow, while the absence of ions keeps glucose and alcohol aqueous mixtures inert."
            )
        )

        // Science 2023 - Q1 (Biology - Life Processes)
        questions.add(
            Question(
                id = "science_2023_q1",
                paperId = "science_2023",
                subjectId = "science",
                year = 2023,
                chapter = "Life Processes",
                questionNumber = "Section C, Q18",
                marks = 3,
                questionText = "State the main functions of: \n" +
                        "1. Bile juice \n" +
                        "2. Villi in the small intestine \n" +
                        "3. Salivary amylase",
                hint = "Think about digestion catalysts and surface area expansion.",
                solutionText = "### Functions:\n\n" +
                        "#### 1. Bile Juice (produced by Liver, stored in gallbladder):\n" +
                        "- **Emulsification of Fats:** Breaks down large fat globules into tiny droplets, increasing the surface area for pancreatic lipase action.\n" +
                        "- **Alkalizing Food:** Neutralizes acidic chyme entering from the stomach to enable pancreatic enzyme action in the small intestine.\n\n" +
                        "#### 2. Villi (Finger-like projections in the small intestine):\n" +
                        "- **Increase Surface Area:** Exponentially boosts the surface area of inner intestinal walls to maximize absorption of digested nutrients.\n" +
                        "- **Wealthy Blood Supply:** Heavily lined with blood capillaries that absorb and transport nutrients directly into the bloodstream.\n\n" +
                        "#### 3. Salivary Amylase (Digestive enzyme in saliva):\n" +
                        "- **Starch Digestion:** Breaks down complex carbohydrates/starches into simple sugars (like maltose) in the mouth.\n" +
                        "\$\$ \\text{Starch} \\xrightarrow{\\text{Salivary Amylase}} \\text{Maltose (Sugars)} \$\$"
            )
        )


        // ================= ENGLISH QUESTIONS =================
        // English 2025 - Q1 (Literature - Nelson Mandela)
        questions.add(
            Question(
                id = "english_2025_q1",
                paperId = "english_2025",
                subjectId = "english",
                year = 2025,
                chapter = "Nelson Mandela - Long Walk to Freedom",
                questionNumber = "Section C, Q10",
                marks = 3,
                questionText = "What are the 'twin obligations' that Nelson Mandela refers to in his autobiography?",
                hint = "Mandela talks about social duties towards local relations vs national civic duties.",
                solutionText = "### Model Answer:\n\n" +
                        "According to Nelson Mandela, every man has **two structural obligations** in life:\n\n" +
                        "1. **The Private Obligation (Personal life):** \n" +
                        "This refers to a person's duty to their family, parents, spouse, and children. It is the intimate social unit of support.\n\n" +
                        "2. **The Public Obligation (Civic life):** \n" +
                        "This represents a person's duty to their community, their fellow countrymen, and their nation.\n\n" +
                        "**Context addition:** Mandela explains that under the brutal regime of Apartheid, it was virtually impossible for a colored person in South Africa to fulfill both. Doing duty for the public ultimately meant separation and forced isolation from family."
            )
        )

        // English 2024 - Q1 (Grammar - Tenses)
        questions.add(
            Question(
                id = "english_2024_q1",
                paperId = "english_2024",
                subjectId = "english",
                year = 2024,
                chapter = "Grammar - Editing Clause",
                questionNumber = "Section B, Q4",
                marks = 1,
                questionText = "Fill in the blank with the correct tense of the verb given in brackets:\n\n" +
                        "\"The students ______ (practice) for the annual tournament since May.\"",
                hint = "Note the keyword 'since May', indicating an action starting in the past and continuing into the present.",
                solutionText = "### Explanation:\n\n" +
                        "- The phrase **'since May'** represents a duration window continuing into the current moment. This necessitates the use of the **Present Perfect Continuous Tense**.\n" +
                        "- Subject: **'The students'** (plural nouns).\n" +
                        "- Present Perfect Continuous formula: **have/has + been + verb-ing**.\n" +
                        "- Conjugation: **have been practicing**.\n\n" +
                        "**Correct Sentence:** \"The students **have been practicing** for the annual tournament since May.\""
            )
        )


        // ================= SOCIAL SCIENCE QUESTIONS =================
        // Social Science 2025 - Q1 (History - Nationalism)
        questions.add(
            Question(
                id = "social_science_2025_q1",
                paperId = "social_science_2025",
                subjectId = "social_science",
                year = 2025,
                chapter = "Nationalism in Europe",
                questionNumber = "Section C, Q14",
                marks = 5,
                questionText = "Explain the core steps in the unification process of Germany in 1871.",
                hint = "Focus on Otto von Bismarck, Prussian army, and the three major wars.",
                solutionText = "### Comprehensive historical breakdown:\n\n" +
                        "1. **Prussian Military Leadership:** After the failure of the liberal Frankfurt Parliament in 1848, the Prussian state undertook active leadership of the national integration movement.\n\n" +
                        "2. **Otto von Bismarck's Strategy:** The Chief Minister of Prussia, **Otto von Bismarck**, orchestrated the movement using the Prussian army and internal bureaucracy (often named the policy of 'Blood and Iron').\n\n" +
                        "3. **Three Major Wars (Over 7 Years):** Prussia fought and won three historical wars against competitors to unify German states:\n" +
                        "   - Against **Denmark** (1864)\n" +
                        "   - Against **Austria** (1866)\n" +
                        "   - Against **France** (1870-1871)\n\n" +
                        "4. **German Empire Proclamation:** Inside the cold Hall of Mirrors at Versailles on **January 18, 1871**, Kaiser William I of Prussia was officially declared the supreme German Emperor.\n\n" +
                        "**Conclusion:** Unification centered around Prussian military might, asserting administrative strength over Central European confederate regions."
            )
        )

        // Social Science 2024 - Q1 (Geography - Soils)
        questions.add(
            Question(
                id = "social_science_2024_q1",
                paperId = "social_science_2024",
                subjectId = "social_science",
                year = 2024,
                chapter = "Resources and Development",
                questionNumber = "Section B, Q5",
                marks = 3,
                questionText = "Distinguish between **Khadar** and **Bangar** alluvial soils.",
                hint = "Khadar is new silt, Bangar is older clay containing calcareous nodules.",
                solutionText = "### Contrast comparison Table:\n\n" +
                        "| Criteria | Khadar Soil | Bangar Soil |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Age** | New alluvial soil. | Older alluvial soil. |\n" +
                        "| **Location** | Found close to low-lying river plains, regularly flooded. | Found in highland terraces away from active river beds. |\n" +
                        "| **Composition** | Highly fine silt clay, extremely fertile. | Rich in calcium carbonate deposits (Kankar nodules). |\n" +
                        "| **Texture** | Sandier, porous texture. | Clayey, heavy and dense texture. |\n" +
                        "| **Fertility** | Highly fertile, ideal for intensive cultivation. | Relatively less fertile; requires chemical fertilizers. |"
            )
        )


        // ================= ODIA QUESTIONS =================
        // Odia 2025 - Q1 (Grammar - Sandhi)
        questions.add(
            Question(
                id = "odia_2025_q1",
                paperId = "odia_2025",
                subjectId = "odia",
                year = 2025,
                chapter = "Odia Grammar (ସନ୍ଧି)",
                questionNumber = "Section A, Q2",
                marks = 1,
                questionText = "ସନ୍ଧି ବିଚ୍ଛେଦ କର : **ମହୋତ୍ସବ**",
                hint = "ସୁର ଓ ଅସୁର ମିଳିତ ସ୍ଵର ସନ୍ଧି ନିୟମ ଅନୁଷ୍ଠାନ କର |",
                solutionText = "### ସମାଧାନ ଓ ସମ୍ପୂର୍ଣ୍ଣ ବ୍ୟାଖ୍ୟା:\n\n" +
                        "- **ସନ୍ଧି ବିଚ୍ଛେଦ:** \n" +
                        "  \$\$ ମହୋତ୍ସବ = ମହା + ଉତ୍ସବ \$\$\n\n" +
                        "- **ନିୟମ ସୂତ୍ର:**\n" +
                        "  ଆମର ସ୍ୱର ସନ୍ଧି ନିୟମ ଅନୁସାରେ:\n" +
                        "  \$\$ ଆ (ମହା) + ଉ (ଉତ୍ସବ) = ଓ (ମହୋତ୍ସବ) \$\$\n" +
                        "  ନିୟମ: **'ଅ' କିମ୍ବା 'ଆ' ପରେ 'ଉ' କିମ୍ବା 'ଊ' ରହିଲେ ଦୁହେଁ ମିଶି 'ଓ' ହୁଅନ୍ତି ।** ଏହା ବର୍ଣ୍ଣର ଓକାର ରୂପରେ ପୂର୍ବ ବ୍ୟଞ୍ଜନରେ ଯୁକ୍ତ ହୁଏ।"
            )
        )

        return questions
    }
}
