# Cafetería

Java 17 model based on the attached Visual Paradigm class diagram. The repository
was empty at the start of this work, so this project uses the Java types shown in
the UML and a conventional Maven layout; no existing application framework,
database schema, or UI toolkit was available to preserve.

## Model interpretation

- The class-by-class and relationship-by-relationship reading of the diagram is
  documented in [docs/uml-analysis.md](docs/uml-analysis.md).
- **Boundary/screens:** screen-to-component links are simple associations.
  The confirmed screen-local form/view links are 1:1 compositions:
  `Login–FormularioLogin`,
  `PantallaComedor–VistaAlimentos/FormularioBusqueda`,
  `PantallaPago–FormularioPago`, and
  `PantallaCajero–vistaPedidosCajero`. Application controls and other
  non-form/view collaborators remain simple associations.
- **Domain associations:** collections in the diagram are represented as
  `List<T>` at the boundary or repository API. A collection does not imply
  composition; persistence/lifecycle ownership is not stated by the diagram.
- **Persistence:** `IDataHelperSQL<T, K>` defines the shared CRUD contract, and
  the domain-specific helper interfaces retain the diagram's search operations.
  `ConnectionDB` delegates connection creation to JDBC using supplied settings.
- **Types:** monetary values use `BigDecimal` rather than binary floating point;
  dates use immutable `java.time` types. The QR matrix uses the diagram's
  `BitMatrix` type from ZXing.

## Classes and relationships represented

| Diagram class/group | Implementation |
| --- | --- |
| `Alimento`, `Factura`, `LineaFactura`, `Mesa`, `Reporte`, `Reserva`, `Ticket`, `Usuario` | `domain` value records with the attributes visible in the diagram |
| `Formulario*`, `Vista*`, `VentanaError`, `SelectorVentanas`, and `Login` / `Pantalla*` | `ui.boundary` boundary state and screen objects |
| Payment, reservation, food selection, ticket, authentication, report, and invoice controls | `application` interfaces representing the diagram's operation contracts |
| Ticket manager | `DefaultGestorTicket` delegates to an injected DAO |
| `IDataHelperSQL`, specialized data helpers, and named `*DAO` types | `persistence` contracts; in-memory CRUD/search adapters are supplied where the diagram defines all query fields |
| `ConnectionDB` | JDBC connection utility |

Customer-service requests, FAQ browsing, and review submission are outside the
current project scope. Their screen, forms/views, domain types, managers, DAO
contracts/adapters, and FAQ-specific chart operation are not implemented.

Concrete payment, authentication, report-generation, controller, and SQL DAO
implementations are not included: the diagram omits the UI/runtime technology,
database schema, authentication provider, payment APIs, and behavior for its
referenced types. `Compra` and `Estudiante` are marker interfaces only because
the diagram references them but provides no attributes or lifecycle.
`DefaultGestorTicket` creates a QR payload from the generated ticket ID because
the diagram specifies a QR matrix but no payload format.

## Diagram ambiguities

- Some UML text is too small to read reliably. The PNG embeds a protected
  `mxfile` payload, so the editable source could not be recovered; the rendered
  diagram is the available source.
- [docs/uml-multiplicities.md](docs/uml-multiplicities.md) records each
  remaining relationship, endpoint role, multiplicity, relation kind, and
  whether the value is explicit in the UML or inferred from the implementation.
- The generic helper diagram declares `buscarPorId(id: String)` while
  `eliminar(id: K)` uses its type parameter. The implementation uses `K` for
  both identifiers to keep the generic contract type-safe.
- `LineaFactura` is labeled as such, but its constructors appear to say
  `LineaCompra`; `LineaFactura` is used consistently with its class name.

## Build

Run `mvn test` with JDK 17 or newer.
