# UML analysis

## Scope and reading rules

The supplied HTML is an embedded PNG, not a standalone class diagram. Its
editable `mxfile` payload is protected, so the analysis uses the rendered
diagram and enlarged regions. Unreadable or conflicting text is called out
rather than silently normalized. Customer-service requests, FAQ browsing, and
review submission have been removed from the project at the user's request;
accordingly, the related screen, components, domain types, managers, DAOs, and
FAQ chart operation are not part of the current model.

The diagram distinguishes `boundary` classes (green/pink), application/control
classes (orange), domain classes (yellow-green), DAO classes (blue), and data
helper interfaces (purple). Screen-to-component relations are simple
associations. The user-confirmed screen-owned form/view compositions that remain
are listed below. All other collaborators are associations unless the diagram
shows a distinct dependency or generalization.

The remaining boundary source files are grouped under the
`com.proyectoingsw.cafeteria.ui.boundary` package.

## Boundary classes

| Class | Visible state/operations | Interpreted relationships |
| --- | --- | --- |
| `vistaPedidosCajero` | `visible`; `listarPedidos()`; `mostrarDatosTicket()` | One-to-one composition with `PantallaCajero`, per the confirmed ownership decision. |
| `VentanaError` | `visible`, `mensaje`; `mostrar(mensaje)`, `cerrar()` | Standalone error component; no owning screen is legible in the diagram. |
| `PantallaCajero` | `vistaPedidosCajero`, two `ControladorTickets` roles, `PagarEfectivo`, `FacturaControlador` | Composes one private view; controller/payment references are simple associations. The repeated controller role is present in the UML and retained. |
| `PantallaEstadisticas` | `CalculadorEstadisticas`, `GeneradorGraficos`; `seleccionMesa()`, `MesasDisponibles()` | Simple associations to calculation and graph-generation controls. The operations' names do not align clearly with the screen's purpose. |
| `Login` | `visible`, `FormularioLogin`, `AutenticarUsuarioControlador`, `Usuario`; `mostrar()`, `cerrar()` | Composes one private form; user and authentication references are simple associations. |
| `FormularioLogin` | `visible`; `validarCampos()`, `obtenerMetodoAutenticacion()`, `obtenerParametros()` | Input component; return types for the last two operations are not legible. |
| `SelectorVentanas` | `visible`, `ventanaActual`; `CambiarVentana()`, `GenerarVentana()` | Selects/navigates between screens; exact target type and cardinality are not stated. |
| `FormularioBusqueda` | `campoTexto`; `obtenerCriterioBusqueda()`, `limpiarCampo()` | One-to-one composition with `PantallaComedor`; the operation is shown as returning `void` despite the field being its result. |
| `VistaAlimentos` | `List<Alimento>`; select, show, hide operations | One-to-one composition with `PantallaComedor`; the food role is many-valued. |
| `FormularioPago` | `opcionPago`, `datosPago`; `obtenerOpcionPago()` | One-to-one composition with `PantallaPago`; payment execution is delegated to a control. |
| `PantallaComedor` | Menu, search form, food-selection controller; `solicitarBusqueda()`, `mostrar()` | Composes one menu view and one search form; food-selection controller is a simple association. The exact controller name is OCR-uncertain (`SeleccionarAlimentoControlador`). |
| `MenuPrincipal` | `SelectorVentanas`; `mostrar()`, `cambiarPantalla()` | Selector is a simple association, not a lifecycle-owned child. |
| `PantallaPago` | Form and purchase handler; `mostrarDatosPago()` | Composes one form; purchase handler is a simple association. The diagram shows a navigation/generation flow toward `PantallaTicket`. |
| `PantallaMesasDisponibles` | `List<Mesa>`, reservation handler; `seleccionMesa()` | Simple association to handler; its table collection is many-valued. |
| `PantallaTicket` | Ticket controller; `mostrarTicket()` | Simple association to controller/manager. |

The screenshot contains repeated `FormularioBusqueda`-looking boxes and role
labels; whether these are duplicate class drawings or distinct instances is
unclear. The confirmed per-screen forms/views are private screen-created
instances; controller and service collaborators remain ordinary associations.

## Application and control classes

| Class/group | Visible collaborators and operations |
| --- | --- |
| `PagarEfectivo` | `pagarConEfectivo(): boolean`, `calcularVuelto(): int`, `generarRecibo(): String` |
| `PagarTarjetaOTransferencia` | `pagarConTransferencia()`, `pagarConTarjeta()`, `generarPagoPorPayPhone()` |
| `GestorFactura` | `FacturaDAO`; `generarFactura()`, `imprimirFacturaVirtual()`, `convertirAPDF(Factura)` |
| `GestorTicket` | `TicketDAO`; `generarTicket()`, `validarTicket(Ticket)` |
| `CalcularEstadistica` | `FacturaDAO`, `ReservaDAO`, `ReporteDAO`; calculates sales, food, and reservation metrics and generates reports |
| `GeneradorGrafico` | Generates order bar chart, pie chart, and report; the report return type in the UML appears inconsistent |
| `AutenticarUsuario` | General authentication plus student, personal, and professor authentication operations |
| `ComprarAlimento` | `AlimentoDAO`; starts a `Compra`, renders its cart, and reads a discount coupon |
| `ReservarMesa` | `ReservaDAO`; creates/modifies reservations, checks availability, and gets reservation history |
| `SeleccionarAlimento` | `AlimentoDAO`; checks stock and lists food by type, availability, and menu date |
| `ControladorTicket`, `ControladorTickets`, `FacturaControlador`, `ComprarAlimentoManejador`, `ReservarMesaManejador`, `SeleccionarAlimentoControlador`, `AutenticarUsuarioControlador` | Referenced by boundary classes; complete signatures are not visible. `ControladorTicket`, `ControladorTickets`, and `FacturaControlador` extend their respective manager contracts in code. |

The services' DAO links are references, not entity ownership. Payment provider
behavior, PDF generation, graphics implementation, controller dispatch, coupon
rules, and authentication cannot be derived from the diagram. Ticket creation
uses an ID-based QR payload because the diagram specifies a QR matrix but no
payload format. Other services remain contracts until their missing runtime
behavior is specified.

## Domain classes

| Class | Attributes visible in the diagram | Notes |
| --- | --- | --- |
| `Usuario` | `nombre`, `contrasena` | Has an authentication-request operation. No role or credential-storage policy is specified. |
| `Alimento` | id, name, type, description, stock quantity, unit price | Returned in many-valued menu/list operations and used by purchase/selection operations. |
| `Factura` | id, date, order number, payment method, total, discount | Factura operations and helper queries refer to this type. |
| `LineaFactura` | line number, quantity, unit price, food subtotal | Constructor text says `LineaCompra`; its association to `Alimento` is not fully legible. |
| `Mesa` | id, reserved flag | Appears in the screen list and reservation operations; an explicit `Reserva` relationship is visible. |
| `Reporte` | id, generation date, time slot, graphs flag, description | Report generation and helper queries refer to it. |
| `Reserva` | id, creation date, time slot, student code, professor ID | The diagram shows a relationship to `Mesa`; the endpoint multiplicities are addressed in the relation register. |
| `Ticket` | id, generation date, valid flag, QR `BitMatrix` | Ticket manager generates and validates it; the diagram shows a connector to `Factura`. |
| `Compra`, `Estudiante`, `Profesor` | Referenced in signatures/fields but no legible class boxes | The diagram omits attributes, constructors, and lifecycle details. |

The implementation uses records for the remaining legible domain boxes.
Monetary values use `BigDecimal`; dates use immutable `java.time` types.
`Compra` and `Estudiante` are marker interfaces because their referenced
classes are not defined in the diagram.

## Persistence classes and interfaces

| Class/interface | Visible contract |
| --- | --- |
| `ConnectionDB` | URL, driver, user, password, JDBC connection; connect/close operations |
| `IDataHelperSQL<T, K>` | add, find by id, read all, update, delete |
| `IAlimentoDataHelper` | Find food by category/type |
| `IFacturaDataHelper` | Find invoices by payment method or date |
| `IMesaDataHelper` | Find tables by reserved state |
| `IReporteDataHelper` | Find reports by date or time slot |
| `IReservaDataHelper` | Find reservations by date, student, time slot, or professor |
| `ITicketDataHelper` | Find tickets by validity |
| `AlimentoDAO`, `FacturaDAO`, `MesaDAO`, `ReporteDAO`, `ReservaDAO`, `TicketDAO` | Each specializes generic CRUD for one domain type and extends its matching search helper. |
| `InMemoryDataHelperSQL<T, K>` | Thread-safe generic CRUD implementation with explicit duplicate/missing-key errors |
| In-memory DAO implementations | Local implementations of the remaining domain DAO contracts for operation and testing |

The generic helper diagram uses `String` for `buscarPorId` but `K` for
`eliminar`. The code uses `K` consistently for both identifiers. Helper search
methods return `List<T>`, preserving visible many-valued results. No concrete
SQL schema or SQL dialect is inferable.

## Relationship register

See [uml-multiplicities.md](uml-multiplicities.md) for the complete endpoint
roles and multiplicities, each relation's UML kind, evidence, and assumptions.
It distinguishes explicit diagram labels from implementation-based cardinality
interpretations and calls out illegible or conflicting notation.
