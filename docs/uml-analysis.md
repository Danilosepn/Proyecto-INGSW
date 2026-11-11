# UML analysis

## Scope and reading rules

The supplied HTML is an embedded PNG, not a standalone HTML class diagram. The
PNG contains a protected `mxfile` text payload; the editable model could not be
decoded. The analysis below uses the rendered diagram and OCR of enlarged
regions. Misspellings and unreadable text are called out rather than silently
normalized. The user confirmed that screen-to-component links are simple
associations, except for the following one-to-one form/view links, which the
user confirmed as **compositions**: `Login–FormularioLogin`,
`PantallaComedor–VistaAlimentos/FormularioBusqueda`,
`PantallaPago–FormularioPago`,
`PantallaServicioCliente–vistaFAQ/FormularioResenia`, and
`PantallaCajero–vistaPedidosCajero`. Those forms/views are screen-exclusive and
screen-owned; screen-to-controller/service and other component links remain
simple associations.

The diagram distinguishes `boundary` classes (green/pink), application/control
classes (orange), domain classes (yellow-green), DAO classes (blue), and data
helper interfaces (purple).

## Boundary classes

| Class | Visible state/operations | Interpreted relationships |
| --- | --- | --- |
| `vistaPedidosCajero` | `visible`; `listarPedidos()`; `mostrarDatosTicket()` | Screen-facing list/detail view; one-to-one composition with `PantallaCajero` per the confirmed ownership decision. |
| `VentanaError` | `visible`, `mensaje`; `mostrar(mensaje)`, `cerrar()` | Standalone error component; may be referenced by a screen. |
| `PantallaCajero` | References `vistaPedidosCajero`, `ControladorTickets`, `PagarEfectivo`, `FacturaControlador`, `ControladorTickets` | Composes one private `vistaPedidosCajero`; controller/payment references are simple associations. The repeated controller spelling/role is present in the diagram. |
| `PantallaEstadisticas` | `CalculadorEstadisticas`, `GeneradorGraficos`; `seleccionMesa()`, `MesasDisponibles()` | Depends on the calculation and graph-generation controls. The latter method names do not align clearly with the screen's purpose. |
| `Login` | `visible`, `FormularioLogin`, `AutenticarUsuarioControlador`, `Usuario`; `mostrar()`, `cerrar()` | Composes one private `FormularioLogin`; user and authentication references are simple associations. |
| `FormularioLogin` | `visible`; `validarCampos()`, `obtenerMetodoAutenticacion()`, `obtenerParametros()` | Input component; return types for the last two operations are not legible. |
| `SelectorVentanas` | `visible`, `ventanaActual`; `CambiarVentana()`, `GenerarVentana()` | Selects/navigates between screens; exact target type is not stated. |
| `FormularioBusqueda` | `campoTexto`; `obtenerCriterioBusqueda()`, `limpiarCampo()` | One-to-one composition with `PantallaComedor`; the method is shown as returning `void` despite the field being its result. |
| `VistaAlimentos` | `List<Alimento>`; select, show, hide operations | One-to-one composition with `PantallaComedor`; its menu role contains many foods. |
| `FormularioPago` | `opcionPago`, `datosPago`; `obtenerOpcionPago()` | One-to-one composition with `PantallaPago`; payment execution is delegated to a control. |
| `PantallaComedor` | Menu, search form, food-selection controller; `solicitarBusqueda()`, `mostrar()` | Composes one menu view and one search form; its food-selection controller is a simple association. The exact controller name is OCR-uncertain (`SeleccionarAlimentoControlador`). |
| `MenuPrincipal` | `SelectorVentanas`; `mostrar()`, `cambiarPantalla()` | The selector is a simple association, not a lifecycle-owned child. |
| `PantallaPago` | Form and purchase handler; `mostrarDatosPago()` | Composes one form; purchase handler is a simple association. |
| `FormularioResenia` | `visible`, `mensaje`, `tema`; `obtenerResenia()` | One-to-one composition with customer-service screen. |
| `PantallaServicioCliente` | FAQ view, review form, FAQ administration, review manager | Composes one FAQ view and one review form; managers are simple associations. |
| `vistaFAQ` | `visible`, FAQ list, `tema`; retrieval operations | One-to-one composition with customer-service screen. |
| `PantallaMesasDisponibles` | `List<Mesa>`, reservation handler; `seleccionMesa()` | Screen-to-handler association; the table list is many-valued. |
| `PantallaTicket` | Ticket controller; `mostrarTicket()` | Controller dependency, not ownership. |

The screenshot contains a few additional `FormularioBusqueda`-looking boxes and
repeated role labels, so the exact number of instances versus duplicate class
drawings is uncertain. The confirmed per-screen forms/views are private,
screen-created instances. Controller and service collaborators remain injected.

## Application and control classes

| Class | Visible collaborators and operations |
| --- | --- |
| `PagarEfectivo` | `pagarConEfectivo(): boolean`, `calcularVuelto(): int`, `generarRecibo(): String` |
| `PagarTarjetaOTransferencia` | `pagarConTransferencia()`, `pagarConTarjeta()`, `generarPagoPorPayPhone()` |
| `GestorFactura` | `FacturaDAO`; `generarFactura()`, `imprimirFacturaVirtual()`, `convertirAPDF(Factura)` |
| `GestorTicket` | `TicketDAO`; `generarTicket()`, `validarTicket(Ticket)` |
| `CalcularEstadistica` | `FacturaDAO`, `ReservaDAO`, `ReporteDAO`; calculates sales, food, and reservation metrics and generates reports |
| `GeneradorGrafico` | Generates order bar chart, pie chart, FAQ summary, and report; one shown return type (`ReporteDAO`) appears inconsistent |
| `AutenticarUsuario` | General authentication plus student, personal, and professor authentication operations |
| `ComprarAlimento` | `AlimentoDAO`; starts a `Compra`, renders its cart, and reads a discount coupon |
| `AdministradorFAQ` | FAQ DAO; obtains and proposes FAQ entries |
| `GestorResenia` | Review DAO; obtains and publishes reviews |
| `ReservarMesa` | `ReservaDAO`; creates/modifies reservations, checks availability, and gets reservation history |
| `SeleccionarAlimento` | `AlimentoDAO`; checks stock and lists food by type, availability, and menu date |
| `ControladorTicket`, `ControladorTickets`, `FacturaControlador`, `ComprarAlimentoManejador`, `ReservarMesaManejador`, `SeleccionarAlimentoControlador`, `AutenticarUsuarioControlador` | Referenced by boundary classes; no complete signatures are visible. |

The service-to-DAO links are dependencies/references, not entity ownership.
Payment provider behavior, PDF generation, graphics implementation, controller
dispatch, coupon rules, and authentication cannot be derived from the diagram.
FAQ and review managers have concrete delegates because their shown operations
map directly to DAO list/add methods. Ticket creation is implemented with an
ID-based QR payload; the diagram does not define the payload format, so this is
a documented local convention. Other services remain contracts until their
missing runtime behavior is specified.

## Domain classes

| Class | Attributes visible in the diagram | Notes |
| --- | --- | --- |
| `Usuario` | `nombre`, `contrasena` | Has an authentication-request operation. No role or credential-storage policy is specified. |
| `Alimento` | id, name, type, description, stock quantity, unit price | Referenced as a many-valued menu/list result and by purchase/selection operations. |
| `Factura` | id, date, order number, payment method, total, discount | Factura operations and helper queries refer to this type. |
| `LineaFactura` | line number, quantity, unit price, food subtotal | Its constructor text says `LineaCompra`; class name and field are used here, but relation to `Alimento` is not fully legible. |
| `Mesa` | id, reserved flag | Appears in the screen list and reservation operations. |
| `PreguntaFAQ` | number, response date, category, question, response | FAQ manager and helper operations refer to it. |
| `Reporte` | id, generation date, time slot, graphs flag, description | Report generation and helper queries refer to it. |
| `Reserva` | id, creation date, time slot, student code, professor ID | `Mesa` is involved in reservation operations; an explicit entity link/cardinality is not safely readable. |
| `Resenia` | id, author name, sent date, topic, reviewed flag | Review manager and helper operations refer to it. |
| `Ticket` | id, generation date, valid flag, QR `BitMatrix` | Ticket manager generates and validates it; a connector to invoice/payment appears in the diagram but its cardinality/role is unclear. |
| `Compra`, `Estudiante`, `Profesor`, `FAQ` | Referenced in signatures/fields but no legible class boxes | The diagram omits their attributes, constructors, and lifecycle details. |

The model implementation uses records for the ten fully legible domain boxes.
Monetary values are represented by `BigDecimal`, and dates by immutable
`java.time` types. Those are implementation-quality choices rather than literal
UML primitive types. `Compra` and `Estudiante` are marker interfaces because
their referenced classes are not defined in the diagram.

## Persistence classes and interfaces

| Class/interface | Visible contract |
| --- | --- |
| `ConnectionDB` | URL, driver, user, password, JDBC connection; connect/close operations |
| `IDataHelperSQL<T, K>` | add, find by id, read all, update, delete |
| `IAlimentoDataHelper` | Find food by category/type |
| `IFacturaDataHelper` | Find invoices by payment method or date |
| `IMesaDataHelper` | Find tables by reserved state |
| `IPreguntaFAQDataHelper` | Find FAQs by category or time slot |
| `IReporteDataHelper` | Find reports by date or time slot |
| `IReservaDataHelper` | Find reservations by date, student, time slot, or professor |
| `IReseniaDataHelper` | Find reviews by topic or student |
| `ITicketDataHelper` | Find tickets by validity |
| `AlimentoDAO`, `FacturaDAO`, `MesaDAO`, `PreguntarFAQDAO`, `ReporteDAO`, `ReservaDAO`, `ReseniaDAO`, `TicketDAO` | DAO contracts associate each entity with its matching helper. |
| `InMemoryDataHelperSQL<T, K>` | Thread-safe generic CRUD implementation with explicit duplicate/missing-key errors |
| `InMemoryAlimentoDAO`, `InMemoryFacturaDAO`, `InMemoryMesaDAO`, `InMemoryPreguntaFAQDAO`, `InMemoryReporteDAO`, `InMemoryReservaDAO`, `InMemoryReseniaDAO`, `InMemoryTicketDAO` | In-memory DAO implementations for local operation and testing |

The generic helper diagram uses `String` for `buscarPorId` but `K` for
`eliminar`. The code uses `K` consistently for both identifiers. Helper search
methods return `List<T>`, preserving visible many-valued results. No concrete
SQL schema or SQL dialect is inferable. Named DAO types are interfaces, with
thread-safe in-memory implementations for the model helpers whose filters map
to visible fields. The FAQ time-slot and review-student searches instead take
explicit key extractors because the corresponding fields are not present on
those entities.

## Relationship and multiplicity decisions

1. **Screen to form/view:** use a one-to-one composition for the seven confirmed
   screen-local form/view roles. The screen creates and privately stores each
   object, with no public child accessor or constructor-injection path; this
   encodes exclusive ownership. In Java, object reclamation is still garbage
   collected, and these state-only classes have no explicit disposal resources.
2. **Other screen-to-component links:** use simple associations as explicitly
   corrected by the user. Controllers, services, selector, and domain references
   are injected and can be shared independently of screen lifetime.
3. **Controller/service to DAO:** association through a DAO field where visible.
   The DAO and domain objects are not composed by a service.
4. **DAO to helper:** the class/interface and dashed realization relationship
   indicate each DAO implements its domain helper, and all share the generic
   CRUD helper. No database table ownership or cascade rule is shown.
5. **Collections and scalar roles:** explicit `List<T>` declarations indicate
   many-valued roles. Readable endpoint `1` labels are retained as a single
   association where applicable. Tiny/overlapping endpoint labels and unlabeled
   links are treated as unknown, not guessed as `0..1`, `1..*`, or composition.
6. **Domain-to-domain links:** only relationships supported by typed attributes
   and operation signatures are represented. For example, `Reserva` stores
   participant identifiers as strings even though a method accepts student
   objects; the diagram does not provide enough information to invent a richer
   `Estudiante`/`Profesor` model.
