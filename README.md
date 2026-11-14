# Cafetería

Aplicación de escritorio Java 17 para una cafetería universitaria. El proyecto
incluye interfaz Swing, modelo de dominio, persistencia JDBC con H2 embebido y
pruebas automatizadas. La estructura sigue el diagrama de clases y conserva el
esquema SQL en `src/main/resources/db/schema.sql`.

Para configurar y ejecutar la aplicación con su base de datos local, consulta
el [tutorial de instalación y ejecución](docs/tutorial-base-datos.md). Las
diferencias verificables entre los archivos Java adjuntos y la implementación,
junto con las recomendaciones para actualizar el UML, se resumen en
[correcciones de lógica y UML](docs/correcciones-logica-uml.md).

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
  The H2 JDBC schema is initialized idempotently from
  `src/main/resources/db/schema.sql`. The desktop app uses a local file database
  at `./data/cafeteria`; food, tables, tickets, invoices, reservations, reports,
  and invoice lines have JDBC DAO adapters.
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
| `IDataHelperSQL`, specialized data helpers, and named `*DAO` types | `persistence` contracts with H2 JDBC adapters for domain CRUD/search; in-memory adapters remain available for tests |
| `ConnectionDB` | JDBC connection utility |

Customer-service requests, FAQ browsing, and review submission are outside the
current project scope. Their screen, forms/views, domain types, managers, DAO
contracts/adapters, and FAQ-specific chart operation are not implemented.

The app seeds the food catalog and table list only when their database tables
are empty. Changes to those rows and generated tickets survive restarts.
Authentication and payment providers are not included, and the domain's
`Compra` and `Estudiante` are marker interfaces without the identity/cart data
needed for those workflows. Their UI actions report the missing integration
instead of claiming success. Reservation-to-table and invoice-to-ticket links
have foreign-key-backed association tables; the domain's current CRUD models
do not expose every relation or support all reservation workflows yet.
`PantallaEstadisticas` exposes its selected table as context, but current
statistics contracts do not accept a table and therefore remain global.
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

Run `mvn clean test package` with JDK 17 or newer. To launch the Swing
application in a desktop environment, run `mvn exec:java`; the menu navigates
between the active screens and the H2 database is created on first launch.
Back up or remove `./data/cafeteria.mv.db` to manage the local database file.

The headless UI tests lay out and paint every screen, exercise each button's
actual hit region, and verify that clicks outside controls do not navigate.
The database integration tests use unique in-memory databases plus an isolated
temporary file-backed database; they verify schema creation, first-run seeds,
CRUD/search, foreign-key associations, constraints, rollback, and persistence
after reopening. These tests do not alter `./data/cafeteria` and do not replace
a visual check in a desktop environment.
