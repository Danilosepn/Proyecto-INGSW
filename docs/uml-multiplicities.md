# UML relationship, role, and multiplicity register

This register covers the model that remains after removing customer-service
requests, FAQ, and review submission. It records both endpoint roles for each
relationship, the relation kind, and the evidence for its multiplicity. The
diagram is an embedded rendered image with an inaccessible editable payload;
where a label cannot be read, the register says so rather than supplying a
cardinality as if it were UML fact.

## Reading the multiplicities

- `1`, `0..1`, `0..*`, and `1..*` have their standard UML meanings.
- **Not specified** means the image does not show a legible multiplicity at
  that endpoint. A singular Java field is recorded as one reference held by
  each source instance, but does not prove a global inverse cardinality.
- For screen-owned forms/views, 1:1 composition is a user-confirmed design
  decision implemented as private, per-screen instances. The removed customer
  service screen's form/view links are intentionally absent.
- Multiplicities inferred from a `List` are `0..*`, because an empty list is
  valid in the implementation. This is an implementation interpretation, not
  an additional UML label.
- A method parameter, return type, or navigation arrow is a dependency/flow,
  not automatically a durable object association. Such relationships are
  called out separately.

## Boundary classes and UI relationships

| End A / role at A | End B / role at B | Multiplicity (A : B) | Kind | Evidence / qualification |
| --- | --- | --- | --- | --- |
| `Login` / owns `formulario` | `FormularioLogin` / belongs to `Login` | `1 : 1` | Composition | User-confirmed ownership; one private form is created per screen. |
| `Login` / refers to `autenticacion` | `AutenticarUsuarioControlador` / authentication collaborator | `1 : not specified` | Simple association | Singular screen field; diagram shows controller role, not an inverse count. |
| `Login` / refers to `usuario` | `Usuario` / current user | `1 : not specified` | Simple association | Singular screen field; no ownership or multiplicity label is legible. |
| `PantallaCajero` / owns `vistaPedidos` | `VistaPedidosCajero` / belongs to cashier screen | `1 : 1` | Composition | User-confirmed ownership; private view created per screen. |
| `PantallaCajero` / `controladorTickets` | `ControladorTickets` / ticket controller | `1 : not specified` | Simple association | Singular role shown in UML and represented as a field. |
| `PantallaCajero` / `controlTickets` | `ControladorTickets` / ticket controller | `1 : not specified` | Simple association | Second, duplicate-looking controller role is retained because it appears separately in the diagram and code. |
| `PantallaCajero` / `pagos` | `PagarEfectivo` / cash-payment control | `1 : not specified` | Simple association | Singular screen reference; inverse sharing count is not stated. |
| `PantallaCajero` / `controlFacturas` | `FacturaControlador` / invoice control | `1 : not specified` | Simple association | Singular screen reference; inverse sharing count is not stated. |
| `PantallaEstadisticas` / `calculador` | `CalculadorEstadisticas` / statistics control | `1 : not specified` | Simple association | Singular screen field; no lifecycle ownership. |
| `PantallaEstadisticas` / `graficos` | `GeneradorGraficos` / graph control | `1 : not specified` | Simple association | Singular screen field; no lifecycle ownership. |
| `PantallaEstadisticas.seleccionMesa()` / selected statistics context | `Mesa` / selected table returned to caller | `1 : 0..1 per call` | Operation dependency / return | The screen-local selector returns at most one selected `Mesa` (none if no options exist); this is not a persistent field link to `PantallaMesasDisponibles`. Both screens receive options from the app's in-memory DAO independently. Current statistics service methods do not accept `Mesa`, so results are still global. |
| `PantallaComedor` / owns `menu` | `VistaAlimentos` / belongs to dining screen | `1 : 1` | Composition | User-confirmed ownership; one private view per screen. |
| `PantallaComedor` / owns `busqueda` | `FormularioBusqueda` / belongs to dining screen | `1 : 1` | Composition | User-confirmed ownership; one private form per screen. |
| `PantallaComedor` / `controlAlimentos` | `SeleccionarAlimentoControlador` / food-selection control | `1 : not specified` | Simple association | Controller name is OCR-uncertain; singleton screen reference in code. |
| `VistaAlimentos` / `menuAlimentos` | `Alimento` / listed food | `1 : 0..*` | Simple association | `List<Alimento>` in the boundary; a list does not imply ownership. |
| `MenuPrincipal` / `selectorVentanas` | `SelectorVentanas` / navigation selector | `1 : not specified` | Simple association | Singular field; user-directed rule treats screen components as associations, not composition. |
| `PantallaPago` / owns `formulario` | `FormularioPago` / belongs to payment screen | `1 : 1` | Composition | User-confirmed ownership; one private form per screen. |
| `PantallaPago` / `control` | `ComprarAlimentoManejador` / purchase handler | `1 : not specified` | Simple association | Singular screen reference; lifecycle is independent. |
| `PantallaMesasDisponibles` / `mesas` | `Mesa` / displayed table | `1 : 0..*` | Simple association | `List<Mesa>`; the UI holds a collection without claiming table ownership. |
| `PantallaMesasDisponibles` / `controlMesas` | `ReservarMesaManejador` / reservation handler | `1 : not specified` | Simple association | Singular screen reference. |
| `PantallaTicket` / `controlTicket` | `ControladorTicket` / ticket control | `1 : not specified` | Simple association | Singular screen reference; controller remains independent. |
| `PantallaComedor` / next screen in flow | `PantallaPago` / payment destination | Not specified | Navigation/dependency | Diagram labels a continuation flow; no endpoint multiplicities or stored association are legible. |
| `PantallaPago` / generated or opened destination | `PantallaTicket` / ticket destination | Not specified | Navigation/dependency | Diagram indicates a ticket-generation/navigation flow; no endpoint multiplicities are legible. |

For all simple associations above, the first multiplicity is the number of
referenced component objects held per source screen. The inverse cardinality
is deliberately left unspecified: the simple-association decision permits
components to outlive a screen but does not assert how many screens may share
one component.

## Domain relationships

| End A / role at A | End B / role at B | Multiplicity (A : B) | Kind | Evidence / qualification |
| --- | --- | --- | --- | --- |
| `Reserva` / `listMesa` (role text adjacent to Mesa end) | `Mesa` / participates in reservation | `1 : 1..*` | Association | The rendered diagram shows `1` at the Reserva end and `1..*` at the Mesa end. Read literally: each reservation links to one or more tables, and each Mesa links to exactly one Reserva. This may oversimplify historical/reusable table reservations; it is retained as drawn. |
| `Factura` / `ticket` (role label placement unclear) | `Ticket` / invoice's ticket | `1 : 1` | Association | Visible `1` labels at both ends; the nearby role text appears to read `ticket`, but its exact endpoint placement is uncertain. |

The diagram also shows `LineaFactura` and `Alimento`, but the rendered connector
and endpoint labels between them are not legible enough to establish a
relationship or multiplicity. No association is invented. `Reserva` stores
participant IDs as strings in the code; operations accepting student/professor
values do not establish persistent entity links or cardinalities.

## Application-to-persistence relationships

| End A / role at A | End B / role at B | Multiplicity (A : B) | Kind | Evidence / qualification |
| --- | --- | --- | --- | --- |
| `GestorFactura` / invoice store | `FacturaDAO` / invoice DAO | `1 : not specified` | Simple association | Singular DAO collaborator in the UML contract. |
| `GestorTicket` / ticket store | `TicketDAO` / ticket DAO | `1 : not specified` | Simple association | Singular collaborator shown; `DefaultGestorTicket` holds one injected DAO. |
| `CalcularEstadistica` / invoice source | `FacturaDAO` / invoice DAO | `1 : not specified` | Simple association | Singular DAO role shown in control. |
| `CalcularEstadistica` / reservation source | `ReservaDAO` / reservation DAO | `1 : not specified` | Simple association | Singular DAO role shown in control. |
| `CalcularEstadistica` / report store | `ReporteDAO` / report DAO | `1 : not specified` | Simple association | Singular DAO role shown in control. |
| `GeneradorGrafico` / report collaborator or result | `ReporteDAO` / report DAO | Not specified | Dependency (uncertain) | A DAO-typed report reference/return appears in the source image, but whether it is a field or operation return is unclear. Current `GeneradorGrafico` contract returns `Reporte`, not a DAO. |
| `ComprarAlimento` / food store | `AlimentoDAO` / food DAO | `1 : not specified` | Simple association | Singular DAO collaborator shown in the control. |
| `SeleccionarAlimento` / food store | `AlimentoDAO` / food DAO | `1 : not specified` | Simple association | Singular DAO collaborator shown in the control. |
| `ReservarMesa` / reservation store | `ReservaDAO` / reservation DAO | `1 : not specified` | Simple association | Singular DAO collaborator shown in the control. |
| `ConnectionDB` / current `connection` | `java.sql.Connection` / JDBC connection | `0..1 : 1` | Simple association | Code field is nullable before connection/after close; no UML multiplicity label. |

No application service composes a DAO or domain object. The inverse DAO-to-service
cardinality is not shown and may be shared, so it remains unspecified.

## DAO and helper relationships

Each DAO implements the shared generic CRUD contract and a domain-specific
search interface. These are interface generalizations/realizations, not object
ownership relationships.

| Concrete contract | Generic helper and domain type | Search helper | Multiplicity / role |
| --- | --- | --- | --- |
| `AlimentoDAO` | `IDataHelperSQL<Alimento, Integer>` | `IAlimentoDataHelper` | `T` is one `Alimento` per CRUD argument/result; `leerTodos` and searches return `0..*`. |
| `FacturaDAO` | `IDataHelperSQL<Factura, String>` | `IFacturaDataHelper` | `T` is one `Factura`; list queries return `0..*`. |
| `MesaDAO` | `IDataHelperSQL<Mesa, Integer>` | `IMesaDataHelper` | `T` is one `Mesa`; list queries return `0..*`. |
| `ReporteDAO` | `IDataHelperSQL<Reporte, String>` | `IReporteDataHelper` | `T` is one `Reporte`; list queries return `0..*`. |
| `ReservaDAO` | `IDataHelperSQL<Reserva, Integer>` | `IReservaDataHelper` | `T` is one `Reserva`; list queries return `0..*`. |
| `TicketDAO` | `IDataHelperSQL<Ticket, Integer>` | `ITicketDataHelper` | `T` is one `Ticket`; list queries return `0..*`. |

The generic helpers declare CRUD operations over `T` and `K`, and search
operations return `List<T>`. These operation-level types do not define
composition or cascade deletion. The UML's `buscarPorId(id: String)` conflicts
with its generic `K`; implementation uses `K` consistently.

## Code-level generalizations and remaining unknowns

| Relationship | Multiplicity | Kind | Qualification |
| --- | --- | --- | --- |
| `ControladorTicket` extends `GestorTicket` | N/A | Generalization | Present in the implementation contracts. |
| `ControladorTickets` extends `GestorTicket` | N/A | Generalization | Present in the implementation contracts. |
| `FacturaControlador` extends `GestorFactura` | N/A | Generalization | Present in the implementation contracts. |
| `InMemoryDataHelperSQL<T, K>` / key extractor | `1 : 1` per adapter instance | Simple association | Implementation-only `Function<T, K>` collaborator; not a UML domain association. |

The image does not yield trustworthy multiplicities for the inverse ends of
most screen-component and controller-DAO links. Exact cardinalities for
operation-only dependencies and any `LineaFactura` links remain unknown. The
user-confirmed compositions are the only ownership relationships introduced
between a screen and its components; customer-service compositions were
removed along with that feature.
