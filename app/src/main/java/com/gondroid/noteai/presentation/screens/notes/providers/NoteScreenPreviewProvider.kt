package com.gondroid.noteai.presentation.screens.notes.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.presentation.screens.notes.NoteDataState

class NoteScreenPreviewProvider : PreviewParameterProvider<NoteDataState> {
    override val values: Sequence<NoteDataState>
        get() =
            sequenceOf(
                NoteDataState(
                    notes = notes,
                ),
            )
}

val notes =
    mutableListOf<Note>()
        .apply {
            addAll(
                arrayOf(
                    Note(
                        id = "1",
                        title = "Presupuesto 📊",
                        content =
                            """
                            Lleva un control detallado de tus gastos y ahorros cada mes.  
                            ✅ Registra ingresos y egresos 💰  
                            ✅ Evalúa dónde reducir gastos 🔍  
                            ✅ Ahorra para metas a largo plazo 🏡  
                            Un buen presupuesto es clave para la estabilidad financiera.  
                            """.trimIndent(),
                        category = "FINANZAS",
                    ),
                    Note(
                        id = "2",
                        title = "Ejercicio 💪",
                        content =
                            """
                            Mantente activo con una rutina diaria de ejercicios:  
                            🏋️ Levantamiento de pesas  
                            🚴 30 minutos de cardio  
                            🧘 Estiramientos para flexibilidad  
                            La actividad física mejora tu salud y bienestar. ¡No te rindas!  
                            """.trimIndent(),
                        category = "SALUD",
                    ),
                    Note(
                        id = "3",
                        title = "Lista de Compras 🛒",
                        content =
                            """
                            Antes de ir al supermercado, asegúrate de llevar una lista:  
                            🥦 Frutas y verduras frescas  
                            🥩 Proteínas saludables  
                            🥛 Lácteos y cereales  
                            Evita compras impulsivas y aprovecha ofertas. ¡Ahorra dinero!  
                            """.trimIndent(),
                        category = "COMPRAS",
                    ),
                    Note(
                        id = "4",
                        title = "Proyecto Nuevo 🚀",
                        content =
                            """
                            Para iniciar un proyecto exitoso, sigue estos pasos:  
                            1️⃣ Define objetivos claros 🎯  
                            2️⃣ Establece un plan de acción 📅  
                            3️⃣ Asigna tareas y plazos ⏳  
                            4️⃣ Evalúa avances y ajusta estrategias 🔄  
                            ¡La organización es clave para el éxito!  
                            """.trimIndent(),
                        category = "TRABAJO",
                    ),
                    Note(
                        id = "5",
                        title = "Ahorros 💰",
                        content =
                            """
                            Ahorrar es importante para la estabilidad financiera.  
                            🔹 Separa al menos el 10% de tus ingresos cada mes  
                            🔹 Evita compras innecesarias  
                            🔹 Considera inversiones para hacer crecer tu dinero  
                            ¡Cada pequeño esfuerzo suma para el futuro! 📈  
                            """.trimIndent(),
                        category = "FINANZAS",
                    ),
                    Note(
                        id = "6",
                        title = "Reunión 📅",
                        content =
                            """
                            Preparación para una reunión efectiva:  
                            📝 Revisa la agenda con anticipación  
                            🎯 Define puntos clave a discutir  
                            🗣️ Escucha activamente y toma notas  
                            ⏳ Respeta los tiempos establecidos  
                            ¡Una reunión productiva impulsa el trabajo en equipo!  
                            """.trimIndent(),
                        category = "TRABAJO",
                    ),
                    Note(
                        id = "7",
                        title = "Aprender Código 👩‍💻",
                        content =
                            """
                            Para mejorar en programación, sigue estos pasos:  
                            🔹 Practica todos los días ⌨️  
                            🔹 Participa en desafíos de código 🏆  
                            🔹 Lee documentación oficial 📚  
                            🔹 Construye proyectos reales 🔧  
                            La clave es la constancia y la curiosidad. 🚀  
                            """.trimIndent(),
                        category = "APRENDIZAJE",
                    ),
                    Note(
                        id = "8",
                        title = "Viaje Soñado ✈️",
                        content =
                            """
                            Planifica tu próximo viaje:  
                            📍 Escoge el destino perfecto  
                            🎟️ Busca vuelos y alojamiento con descuento  
                            📝 Crea una lista de actividades  
                            🏞️ Explora lugares emblemáticos  
                            Viajar expande tu mente y te llena de experiencias únicas. 🌍  
                            """.trimIndent(),
                        category = "PERSONAL",
                    ),
                    Note(
                        id = "9",
                        title = "Salud y Bienestar 🏋️",
                        content =
                            """
                            Mejora tu bienestar con estos hábitos saludables:  
                            ✅ Bebe suficiente agua 💧  
                            ✅ Duerme al menos 7 horas 😴  
                            ✅ Mantén una dieta equilibrada 🥗  
                            ✅ Evita el estrés con actividades relajantes 🌿  
                            Tu salud es tu mejor inversión. ¡Cuídala!  
                            """.trimIndent(),
                        category = "SALUD",
                    ),
                    Note(
                        id = "10",
                        title = "Reseña 📖",
                        content =
                            """
                            Escribir una buena reseña implica:  
                            ✍️ Explicar la idea principal del libro  
                            🎭 Hablar sobre los personajes y su desarrollo  
                            ⭐ Dar una valoración honesta  
                            📢 Recomendarlo a otros si te gustó  
                            ¡Leer nos hace viajar sin movernos! 📚  
                            """.trimIndent(),
                        category = "OTROS",
                    ),
                    Note(
                        id = "11",
                        title = "Gastos Mensuales 💳",
                        content =
                            """
                            Lleva un registro de tus finanzas con estos consejos:  
                            📌 Registra ingresos y egresos  
                            📌 Evita compras innecesarias  
                            📌 Usa herramientas de control financiero  
                            📌 Ajusta tu presupuesto cada mes  
                            Un buen control financiero = tranquilidad. 😊  
                            """.trimIndent(),
                        category = "FINANZAS",
                    ),
                    Note(
                        id = "12",
                        title = "Rutina de Gym 🏋️",
                        content =
                            """
                            🏃‍♂️ Calentamiento: 10 min de cardio  
                            💪 Ejercicios de fuerza: pesas y resistencia  
                            🧘 Estiramientos para flexibilidad  
                            🔄 Repetir 3-5 veces a la semana  
                            ¡Tu cuerpo te lo agradecerá!  
                            """.trimIndent(),
                        category = "SALUD",
                    ),
                    Note(
                        id = "13",
                        title = "Supermercado 🛍️",
                        content =
                            """
                            🍎 Compra alimentos frescos y nutritivos  
                            🏷️ Compara precios antes de comprar  
                            📋 Usa una lista para evitar olvidos  
                            💳 Aprovecha descuentos y cupones  
                            ¡Organizarse es clave para ahorrar!  
                            """.trimIndent(),
                        category = "COMPRAS",
                    ),
                    Note(
                        id = "14",
                        title = "Metas de Ahorro 📈",
                        content =
                            """
                            💡 Define cuánto quieres ahorrar cada mes  
                            📊 Establece un plan a corto y largo plazo  
                            💳 Evita deudas innecesarias  
                            🎯 Usa una cuenta separada para no gastar  
                            ¡Pequeños esfuerzos crean grandes resultados!  
                            """.trimIndent(),
                        category = "FINANZAS",
                    ),
                    Note(
                        id = "1",
                        title = "Project Deadline",
                        content = "Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday.",
                        category = Category.WORK.toString(),
                    ),
                    Note(
                        id = "2",
                        title = "Grocery List",
                        content = "Milk, eggs, bread, eggs, bread, and coffee.",
                        category = Category.PERSONAL.toString(),
                    ),
                    Note(
                        id = "3",
                        title = "Resumen de Attack on Titan (Shingeki no Kyojin)",
                        content =
                            "La humanidad vive dentro de enormes murallas para protegerse de los titanes, gigantes devoradores de humanos. Eren Jaeger, junto a sus amigos Mikasa y Armin, presencia la destrucción de su hogar cuando el Titán Colosal abre una brecha en la Muralla María. Motivado por la venganza, Eren se une al Cuerpo de Exploración para luchar contra los titanes y descubrir la verdad detrás de su existencia.\n" +
                                "\n" +
                                "A lo largo de la historia, se revelan secretos sobre los titanes, las murallas y la verdadera historia del mundo. Traiciones, conspiraciones y batallas épicas llevan a Eren a tomar decisiones que cambiarán el destino de la humanidad.\n" +
                                "\n" +
                                "Es una historia llena de acción, drama y giros impactantes que explora la libertad, la guerra y la lucha por la supervivencia",
                        category = Category.WORK.toString(),
                    ),
                    Note(
                        id = "4",
                        title = "Book Recommendation",
                        content = "Read 'Atomic Habits' by James Clear.",
                        category = Category.LEARNING.toString(),
                    ),
                    Note(
                        id = "5",
                        title = "Workout Plan",
                        content = "Cardio on Monday, Strength on Tuesday.",
                        category = Category.HEALTH.toString(),
                    ),
                    Note(
                        id = "6",
                        title = "Birthday Reminder",
                        content = "Buy a gift for Sarah's birthday.",
                        category = Category.PERSONAL.toString(),
                    ),
                    Note(
                        id = "7",
                        title = "App Idea",
                        content = "A habit tracker with AI recommendations.",
                        category = Category.WORK.toString(),
                    ),
                    Note(
                        id = "8",
                        title = "Travel Checklist",
                        content = "Passport, tickets, charger, and toiletries.",
                        category = Category.PERSONAL.toString(),
                    ),
                    Note(
                        id = "9",
                        title = "Course Notes",
                        content = "Key takeaways from the Kotlin coroutines course.",
                        category = Category.LEARNING.toString(),
                    ),
                    Note(
                        id = "10",
                        title = "Budget Plan",
                        content = "Track monthly expenses and savings.",
                        category = Category.FINANCE.toString(),
                    ),
                    Note(
                        id = "11",
                        title = "Travel Checklist",
                        content = "Passport, tickets, charger, and toiletries.",
                        category = Category.PERSONAL.toString(),
                    ),
                    Note(
                        id = "12",
                        title = "Course Notes",
                        content = "Key takeaways from the Kotlin coroutines course.",
                        category = Category.LEARNING.toString(),
                    ),
                    Note(
                        id = "13",
                        title = "Budget Plan",
                        content = "Track monthly expenses and savings.",
                        category = Category.FINANCE.toString(),
                    ),
                ),
            )
        }
