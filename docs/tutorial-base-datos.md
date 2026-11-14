# Tutorial: ejecutar la aplicación y su base de datos local

## Cómo funciona la base de datos

La aplicación usa **H2 en modo embebido**: no necesita instalar ni iniciar
MySQL, PostgreSQL, Docker u otro servidor. Al arrancar, `JdbcDatabase` carga
`src/main/resources/db/schema.sql` y crea las tablas que todavía no existan.
La base de datos local se guarda en `./data/cafeteria` relativo al directorio
desde el que se ejecuta Maven; el archivo principal es
`data/cafeteria.mv.db`.

En la primera ejecución, la aplicación también agrega tres alimentos de muestra
y seis mesas, pero solo si las tablas correspondientes están vacías. Los
cambios se conservan entre ejecuciones.

## Requisitos

- JDK 17 o posterior (`java -version` debe mostrar esa versión).
- Apache Maven instalado y disponible como `mvn` en la terminal.
- Entorno de escritorio para abrir la interfaz Swing.
- Conexión a Internet la primera vez que Maven necesite descargar dependencias.

## Descargar el proyecto

En PowerShell, clona la rama que contiene la aplicación, la persistencia y el
esquema SQL:

```powershell
git clone --branch danilosepn-implementar-modelo-uml https://github.com/Danilosepn/Proyecto-INGSW.git
Set-Location Proyecto-INGSW
```

Si ya tienes el repositorio, abre PowerShell en su carpeta y selecciona esa
rama con `git switch danilosepn-implementar-modelo-uml`. Antes de cambiar de
rama, guarda o confirma tus cambios locales.

## Compilar, probar y ejecutar

Desde la carpeta que contiene `pom.xml`, comprueba las herramientas y ejecuta
las pruebas:

```powershell
java -version
mvn -version
mvn clean test
```

Cuando Maven termine sin errores, inicia la interfaz:

```powershell
New-Item -ItemType Directory -Force .\data
mvn exec:java
```

La aplicación inicializa el esquema al abrir la base local por primera vez;
no es necesario importar el SQL manualmente ni configurar credenciales. Para
comprobar que se creó el archivo después de cerrar o iniciar la aplicación:

```powershell
Test-Path .\data\cafeteria.mv.db
```

El comando debe devolver `True` una vez que H2 haya abierto la base basada en
archivo. El directorio `data` está excluido de Git, así que los datos locales no
se agregan al repositorio.

## Copia de seguridad y conservación de datos

Cierra la aplicación antes de copiar o mover los archivos de H2. Puedes guardar
una copia del archivo principal con:

```powershell
Copy-Item .\data\cafeteria.mv.db .\data\cafeteria-respaldo.mv.db
```

No borres ni reemplaces la base para resolver un error sin respaldarla primero:
contiene las modificaciones locales. Si necesitas empezar con una base vacía,
cierra la aplicación, conserva una copia fuera de `data` y luego cambia el
nombre del archivo actual. La siguiente ejecución creará un archivo nuevo y
volverá a aplicar el esquema.

## Problemas frecuentes

| Síntoma | Qué revisar |
| --- | --- |
| `mvn` no se reconoce | Instala Apache Maven, agrega su carpeta `bin` al `PATH` y abre una terminal nueva. Confirma con `mvn -version`. |
| Java es anterior a 17 o Maven usa otro Java | Instala JDK 17+, configura `JAVA_HOME` para ese JDK y verifica `java -version` y `mvn -version`. |
| Maven no puede descargar dependencias | Comprueba la conexión/proxy y vuelve a ejecutar `mvn clean test`; no elimines la base de datos para resolver un fallo de descarga. |
| No aparece `data\cafeteria.mv.db` | Asegúrate de ejecutar `mvn exec:java` desde la carpeta del proyecto y revisa que el usuario tenga permiso de escritura allí. |
| La aplicación dice que no pudo iniciar la base | Cierra otra instancia que tenga abierto el mismo archivo, verifica permisos de escritura y conserva una copia antes de cualquier reparación manual. |
| La interfaz no abre en una sesión remota o sin pantalla | La aplicación usa Swing y necesita un entorno gráfico; las pruebas automatizadas no requieren abrir la ventana. |

El proyecto crea el esquema con `CREATE TABLE IF NOT EXISTS`; eso prepara una
base nueva, pero no es un mecanismo general de migración para tablas antiguas.
El esquema sí incluye una actualización explícita de la precisión de
`TICKET.FECHA_GENERACION`; otros cambios estructurales en bases existentes
requieren una migración específica.
