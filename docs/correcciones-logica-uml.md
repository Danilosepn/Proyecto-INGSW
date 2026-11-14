# Correcciones verificables de la lógica y recomendaciones para el UML

## Alcance de la revisión

No se encontró una tabla de errores independiente entre los archivos adjuntos
ni en la rama del proyecto. Por tanto, este documento **no pretende reproducir
esa tabla ni atribuirle prioridades**. Registra únicamente diferencias
comprobables en los archivos Java adjuntos y cómo están representadas en el
proyecto actual. El adjunto UML es un HTML que contiene una imagen, no un archivo
de modelo editable.

Las clases adjuntas ya tienen equivalentes dentro del proyecto, bajo
`com.proyectoingsw.cafeteria.domain` y
`com.proyectoingsw.cafeteria.persistence`. La adaptación sigue el diseño usado
por la aplicación: records para los datos de dominio, contratos DAO tipados y
adaptadores JDBC; no se copiaron clases duplicadas al paquete por defecto.

## Diferencias y ajustes

| Evidencia en los adjuntos | Adaptación presente en el proyecto | Qué reflejar o revisar en el UML |
| --- | --- | --- |
| `Factura` crea `new Arraylist<>()`, y `Factura`, `Reserva` y `LineaFactura` usan colecciones sin importar `List`/`ArrayList`. El nombre `Arraylist` tampoco corresponde a `ArrayList`. | Esos tipos de dominio son records y no construyen colecciones. La persistencia y los vínculos se manejan con DAO y tablas relacionales. | No trasladar esos errores de compilación a las firmas del modelo. Dibujar asociaciones solo cuando sus claves y multiplicidades estén definidas. |
| El archivo adjunto se llama `ITicketDalaHelper.java`, pero declara la interfaz pública `ITicketDataHelper`; el nombre del archivo no coincide con el tipo declarado. | La interfaz existente está en `ITicketDataHelper.java` y consulta tickets por validez. | Conservar una sola interfaz `ITicketDataHelper` y alinear con ella el nombre de archivo y el nombre UML. |
| `Alimento` ofrece un constructor con `tipo` entero y lo convierte a texto; el constructor no recibe precio. | `Alimento` usa `String tipo` y `BigDecimal precioUnitario`, de acuerdo con la columna `TIPO` y el precio decimal del esquema. | Mantener `tipo: String` y `precioUnitario: BigDecimal` para que UML, objeto y SQL coincidan. |
| Los adjuntos usan `Date`, `double` para dinero y campos de relaciones (`Factura.ticket`, `Ticket.factura`, `Reserva.listMesa`). | Las fechas usan `LocalDate`/`LocalDateTime`; importes usan `BigDecimal`. Las asociaciones Factura–Ticket y Reserva–Mesa se guardan por separado en `FACTURA_TICKET` y `RESERVA_MESA`. | Mostrar las asociaciones con sus tablas/DAO y no como campos duplicados obligatorios en ambos records. La base permite que la relación esté todavía sin asociar. |
| `Ticket.fechaGeneracion` conserva nanosegundos en Java, pero el esquema H2 usaba la precisión predeterminada de seis dígitos fraccionarios; el tiempo leído de vuelta no era igual al generado. | `TICKET.FECHA_GENERACION` ahora declara `TIMESTAMP(9)` y el inicializador aplica un `ALTER` idempotente también a bases existentes. Una prueba verifica un ticket generado con nanosegundos exactos. | Mantener `LocalDateTime` con la precisión del almacenamiento y anotar la precisión temporal soportada por la base. |
| `LineaFactura` declara alimentos en una lista, pero el adjunto no ofrece un identificador de factura ni de alimento en cada línea. | `LINEA_FACTURA` persiste número, cantidad, precio y subtotal; actualmente no tiene FK a `FACTURA` ni a `ALIMENTO`. | El diagrama no debe implicar una FK que no existe. Si la línea debe pertenecer a una factura y alimento concretos, agregar identificadores a dominio y esquema como un cambio de diseño coordinado. |
| El contrato genérico del adjunto usa `K` para el identificador de eliminación; el UML descrito en la documentación muestra `String` para búsqueda por ID. | `IDataHelperSQL<T, K>` usa `K` tanto en `buscarPorId` como en `eliminar`; cada DAO escoge `Integer` o `String` según su entidad. | Generalizar ambas operaciones como `K` en el diagrama. |

## Clases y relaciones a conservar o aclarar

- **Persistencia:** `IDataHelperSQL<T, K>` es la interfaz común; cada
  `*DAO` añade búsquedas específicas y tiene adaptadores `Jdbc*DAO`. La
  aplicación también conserva adaptadores en memoria para pruebas/demo.
- **Reserva–Mesa:** `JdbcReservaMesaDAO` gestiona la asociación mediante
  `RESERVA_MESA`. Una reserva puede vincularse a varias mesas; el esquema hace
  único `ID_MESA`, por lo que una mesa no puede estar asociada a más de una
  reserva a la vez. El record `Reserva` no contiene una lista de mesas.
- **Factura–Ticket:** `JdbcFacturaTicketDAO` y `FACTURA_TICKET` implementan una
  asociación uno a uno mediante claves foráneas únicas. La relación puede no
  existir todavía mientras la factura o el ticket estén sin asociar.
- **Factura–LineaFactura–Alimento:** la clase de línea no contiene claves para
  esas entidades y el SQL no establece dichas relaciones. El diagrama debe
  aclarar la intención antes de representarlas como vínculos persistidos.
- **Dinero y fechas:** usar `BigDecimal` para precios, totales y subtotales;
  usar `LocalDate` para fechas de factura/reserva/reporte y `LocalDateTime`
  para generación de ticket.
- **Ticket:** el atributo `codigoQR` se conserva como `BitMatrix`; JDBC lo
  almacena en `TICKET.CODIGO_QR`. El generador construye el QR a partir del
  identificador del ticket porque el UML no define otro formato de contenido.

## Referencias para comprobarlo

- Modelo Java: `src/main/java/com/proyectoingsw/cafeteria/domain/`.
- Interfaces y adaptadores: `src/main/java/com/proyectoingsw/cafeteria/persistence/`.
- Esquema local: `src/main/resources/db/schema.sql`.
- Análisis de clases y multiplicidades: `docs/uml-analysis.md` y
  `docs/uml-multiplicities.md`.
