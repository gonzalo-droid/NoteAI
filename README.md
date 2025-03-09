<h1 align="center">Note.AI</h1>

<p align="center">
  <a href="https://spdx.org/licenses/MIT.html"><img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=26"><img alt="API" src="https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat"/></a><br>
  <a href="https://www.youtube.com/@GonzaloDroid2050"><img alt="Profile" src="https://img.shields.io/youtube/channel/subscribers/UCPjql8JlN5kw6hU2U_tngaw?style=social"/></a> 
  

</p>

<p align="center">  
🤖 Note.AI, proyecto android desarrollado con Jetpack Compose, Hilt, Coroutines, Flow, Jetpack (Room, ViewModel), Material3, arquitectura MVVM.
</p>

<p align="center">
<img src="previews/ss_summary.png"/>
</p>

## Note.AI
Es un proyecto personal que he estado compartiendo mis avances en mis redes sociales, he seguido el desarrollo moderno en android.

- **Notas:** Listado y creación de notas, guardadas en base de datos local.  
- **Grabación de voz:** Notas de voz asociadas a las notas creadas. 
- **Open.AI:** Transcripción de audio a texto usando el modelo Whisper.
- **Configuración Centralizada:** Administra la configuración y claves de API con BuildKConfig.  
- **Interfaz Moderna:** Construida con Kotlin Compose para una experiencia visual atractiva y responsiva.  

## 🛠️ Instalación y Configuración  

##### 1️⃣ Clonar el Repositorio  
```bash
git clone https://github.com/gonzalo-droid/NoteAI.git
```
##### 2️⃣ Generar tu Clave de API KEY en Open.AI Platform
- Visita https://platform.openai.com/docs/overview
- Regístrate o inicia sesión.
- Dirígete a la sección API de tu cuenta y genera una nueva clave de API
- Recuerda que para el correcto funcionamiento debes agregar unos cuantos dolares para realizar las pruebas
##### 3️⃣ Agregar la Clave de API en local.properties
- En la raíz del proyecto, crea (o actualiza) un archivo llamado local.properties y agrega la siguiente línea:
```bash
API_KEY_OPENAI=TU_CLAVE_DE_API_AQUÍ
```
##### 4️⃣ Compilar y Ejecutar el Proyecto
- Usa Gradle para compilar y ejecutar el proyecto:
```bash
./gradlew run
```
Para Android, abre el proyecto en Android Studio y ejecuta la aplicación desde allí. 📱🚀


## Tech stack

- **SDK mínimo:** 26.  
- Basado en [Kotlin](https://kotlinlang.org/), utilizando [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) para operaciones asíncronas.  

- **Jetpack Compose:** Kit de herramientas moderno de Android para desarrollo de UI declarativa.  
- **Lifecycle:** Observa los ciclos de vida de Android y gestiona los estados de UI ante cambios de ciclo de vida.  
- **ViewModel:** Administra datos relacionados con la UI y es consciente del ciclo de vida, asegurando la persistencia de datos tras cambios de configuración.  
- **Navigation:** Facilita la navegación entre pantallas, complementado con [Hilt Navigation Compose](https://developer.android.com/jetpack/compose/libraries#hilt) para inyección de dependencias.  
- **Room:** Permite construir una base de datos con una capa de abstracción sobre SQLite para un acceso eficiente a los datos.  
- **[Hilt](https://dagger.dev/hilt/):** Simplifica la inyección de dependencias en la aplicación.  
- **Arquitectura MVVM (View - ViewModel - Model):** Promueve la separación de responsabilidades y mejora el mantenimiento del código.  
- **Patrón Repository:** Actúa como mediador entre diferentes fuentes de datos y la lógica de negocio de la aplicación.  
- **[Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization):** Serialización sin reflejos para múltiples plataformas y formatos en Kotlin.  
- **[ksp](https://github.com/google/ksp):** API de procesamiento de símbolos en Kotlin para generación y análisis de código.  
- **[ktlint](https://github.com/pinterest/ktlint):** Linter para kotlin. https://medium.com/@amitdogra70512/ktlint-integration-in-android-952049b9d17d
## Architecture
**Note.AI** sigue la aquitectura MVVM e implementa patrón Repository, alineado con [Guía oficl de arquitectura de Google](https://developer.android.com/topic/architecture).

La arquitectura de **Note.AI** está estructurada en dos capas distintas: la capa de UI y la capa de datos. Cada capa cumple roles y responsabilidades específicas, que se describen a continuación.  

**Note.AI** sigue los principios establecidos en la [Guía de arquitectura de aplicaciones](https://developer.android.com/topic/architecture), lo que lo convierte en un excelente ejemplo de la aplicación práctica de conceptos arquitectónicos.  


### Architecture Overview

- Cada capa sigue los principios de [flujo unidireccional de eventos/datos](https://developer.android.com/topic/architecture/ui-layer#udf): la capa de UI envía eventos del usuario a la capa de datos, y la capa de datos proporciona flujos de datos a otras capas.  
- La capa de datos opera de forma independiente de las demás capas, manteniendo su pureza sin depender de capas externas.  

✅ Esta arquitectura desacoplada mejora la reutilización de componentes y la escalabilidad de la aplicación, facilitando su desarrollo y mantenimiento.  


#### 🎨 Capa de UI  

La capa de UI abarca los elementos visuales responsables de configurar las pantallas para la interacción del usuario, junto con el [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), que gestiona los estados de la aplicación y restaura los datos durante los cambios de configuración.  

📌 **Principales características:**  
- Los elementos de UI observan el flujo de datos, asegurando la sincronización con la capa de datos subyacente.  
- Se encarga de manejar las interacciones del usuario y reflejar los cambios en la interfaz de manera eficiente.  

#### 📂 Capa de Datos  

La capa de datos está compuesta por repositorios que manejan la lógica de negocio, como la recuperación de datos desde una base de datos local o la obtención de datos remotos desde la red. Esta capa está diseñada para priorizar el acceso sin conexión, funcionando principalmente como un repositorio *offline-first* de la lógica de negocio.  

📌 **Principales características:**  
- Sigue el principio de **"fuente única de la verdad"** (*single source of truth*), garantizando que todas las operaciones de datos sean centralizadas y consistentes.  
- Gestiona la persistencia de datos y la comunicación con fuentes externas.  

## Open API
Note.AI usa open.ai para la transcripción de las notas de voz a texto.

### Sigamos en contacto

✨ **Espero que este proyecto te sea útil para seguir aprendiendo.**  
💡 ¡Puedes colaborar en mejoras del proyecto dejando un *Pull Request*!  
⭐ Además, agradecería mucho que le dieras una estrella al proyecto 🤩 


Aún estoy definiendo el formato 🫠, pero lo importante es empezar. 
¡Suscríbete y vamos a codear!
- [YouTube](https://www.youtube.com/@gonzalolock)
- [TikTok](https://www.tiktok.com/@gonzalock.dev)
- [LinkedIn](https://www.linkedin.com/in/gonzalo-lozg/)


## 🚀 ¡Contribuciones bienvenidas!  

💡 **Si quieres proponer mejoras o corregir errores:**  
1. Haz un *fork* del repositorio.  
2. Crea una rama con tu mejora.
  ```bash
   git checkout -b feature/your-feature-name
  ```
3. Realiza los cambios y haz un *commit*.
 ```bash
   git commit -am 'Add some feature'
   ```  
4. Sube los cambios a tu repositorio.
 ```bash
   git push origin feature/your-feature-name
   ```  
5. Abre un *Pull Request* para revisión.  


## License 

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.
