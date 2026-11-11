# Cafetería

Java 17 model based on the attached Visual Paradigm class diagram. The repository
was empty at the start of this work, so this project uses the Java types shown in
the UML and a conventional Maven layout; no existing application framework,
database schema, or UI toolkit was available to preserve.

## Model interpretation

- The class-by-class and relationship-by-relationship reading of the diagram is
  documented in [docs/uml-analysis.md](docs/uml-analysis.md).
- **Boundary/screens:** screen-to-component links are simple associations.
  The specifically confirmed form/view links are 1:1 compositions: each screen
  creates and privately owns its own form/view instance. Application controls
  and other non-form/view collaborators remain injected simple associations.
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
| `Alimento`, `Factura`, `LineaFactura`, `Mesa`, `PreguntaFAQ`, `Reporte`, `Reserva`, `Resenia`, `Ticket`, `Usuario` | `domain` value records with the attributes visible in the diagram |
| `Formulario*`, `Vista*`, `VentanaError`, `SelectorVentanas`, and `Login` / `Pantalla*` | `ui` boundary state and screen objects |
| Payment, reservation, food selection, ticket, authentication, report, and invoice controls | `application` interfaces representing the diagram's operation contracts |
| FAQ/review/ticket managers | `DefaultAdministradorFAQ`, `DefaultGestorResenia`, and `DefaultGestorTicket` delegate to injected DAOs |
| `IDataHelperSQL`, specialized data helpers, and named `*DAO` types | `persistence` contracts; in-memory CRUD/search adapters are supplied where the diagram defines all query fields |
| `ConnectionDB` | JDBC connection utility |

Concrete payment, authentication, report-generation, controller, and SQL DAO
implementations are not included: the diagram omits the UI/runtime technology,
database schema, authentication provider, payment APIs, and behavior for its
referenced types. `Compra` and `Estudiante` are marker interfaces only because
the diagram references them but provides no attributes or lifecycle.
`InMemoryPreguntaFAQDAO` and `InMemoryReseniaDAO` require caller-supplied
extractors for search fields absent from their domain classes; no hidden fields
are assumed.
`DefaultGestorTicket` creates a QR payload from the generated ticket ID because
the diagram specifies a QR matrix but no payload format.

## Diagram ambiguities

- Some UML text is too small to read reliably. The PNG embeds a protected
  `mxfile` payload, so the editable source could not be recovered; the rendered
  diagram is the available source.
- The multiplicity at some association ends is unreadable. The implementation
  preserves clear list-valued roles and single-valued attributes without
  guessing missing cardinalities.
- The generic helper diagram declares `buscarPorId(id: String)` while
  `eliminar(id: K)` uses its type parameter. The implementation uses `K` for
  both identifiers to keep the generic contract type-safe.
- `LineaFactura` is labeled as such, but its constructors appear to say
  `LineaCompra`; `LineaFactura` is used consistently with its class name.

## Build

Run `mvn test` with JDK 17 or newer.
