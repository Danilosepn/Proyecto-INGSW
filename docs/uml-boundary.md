# Boundary classes from the original UML

This is a boundary-only transcription of the rendered UML supplied as an HTML
attachment. The original file embeds a protected image rather than editable
diagram data, so capitalization, misspellings, omitted return types, and
unclear endpoints are retained or explicitly marked instead of silently
corrected. It is a reference to the original model, not a claim that every
listed class remains in the current implementation.

Boundary classes are the boxes marked `<<boundary>>` in the diagram. The source
draws some component boxes in green and screens in pink; color is not used as a
different UML type.

## Original class contents

### `vistaPedidosCajero` — present in `ui.boundary`

```text
<<boundary>>
-visible:boolean
+listarPedidos():void
+mostrarDatosTicket():void
```

### `PantallaCajero` — present in `ui.boundary`

```text
<<boundary>>
-vistaPedidos: vistaPedidosCajero
-controladorTickets: ControladorTickets
-pagos: PagarEfectivo
-controlFacturas: FacturaControlador
-controlTickets: ControladorTickets
+PantallaCajero():void
```

The UML has two separate `ControladorTickets` roles (`controladorTickets` and
`controlTickets`); both are copied as drawn.

### `VentanaError` — present in `ui.boundary`

```text
<<boundary>>
-visible: boolean
-mensaje: String
+mostrar(mensaje: String)
+cerrar
```

The diagram does not show a return type for `mostrar` or parentheses after
`cerrar`.

### `PantallaEstadisticas` — present in `ui.boundary`

```text
<<boundary>>
-calculador: CalculadorEstadisitcas
-graficos: GeneradorGraficos
+seleccionMesa(): Mesa
+MesasDisponibles(): void
```

`CalculadorEstadisitcas` is transcribed as it appears in the image; its spelling
and relationship to the control class elsewhere in the UML are inconsistent.
In the GUI, `seleccionMesa()` returns the Mesa selected in this screen's
statistics context; `MesasDisponibles()` navigates to the dedicated table
screen.

### `Login` — present in `ui.boundary`

```text
<<boundary>>
-visible: boolean
-formLoging: FormularioLoging
-autenticacion: AutenticarUsuarioControlador
-usr: Usuario
+Login()
+mostrar()
+cerrar()
```

The UML spells the form role/type `formLoging: FormularioLoging`; the current
code uses `formulario: FormularioLogin`.

### `FormularioLogin` — present in `ui.boundary`

```text
<<boundary>>
-visible
+FormularioLogin()
+validarCampos(): boolean
+obtenerMetodoAutenticacion():
+obtenerParametros():
```

The attribute type for `visible` and the return types of the last two
operations are not shown legibly in the source image.

### `SelectorVentanas` — present in `ui.boundary`

```text
<<boundary>>
-visible: boolean
-ventanaActual: String
+SelectorVentanas()
+CambiarVentana()
+GenerarVentana()
```

### `FormularioBusqueda` — present in `ui.boundary`

```text
<<boundary>>
-campoTexto: String
+obtenerCriterioBusqueda(): void
+limpiarCampo(): void
```

The same-looking `FormularioBusqueda` box appears twice in the rendered
diagram. This transcription treats the repeated drawing as one class; the
diagram does not clarify whether the duplication represents separate instances.

### `VistaAlimentos` — present in `ui.boundary`

```text
<<boundary>>
-menuAlimentos: List<Alimento>
+VistaAlimentos(): void
+seleccionarAlimento(): Alimento
+Ocultar(): void
+mostrar(): void
```

### `Menu Principal` — present in `ui.boundary` as `MenuPrincipal`

```text
<<boundary>>
-visible: boolean
-selectorVentanas: SelectorVentanas
+MenuPrincipal()
+mostrar()
+cambiarPantalla()
```

### `PantallaComedor` — present in `ui.boundary`

```text
<<boundary>>
-menu: VistaAlimentos
-busqueda: FormularioBusqueda
-controlAlimentos: SeleccionarAlimentoControlador
+solicitarBusqueda: List<Alimentos>
+mostrar()
+PantallaComedor()
```

`solicitarBusqueda` is printed with a colon rather than parentheses, and the
return type appears to be `List<Alimentos>` in the diagram, while the
implemented class uses `List<Alimento>`.

### `FormularioPago` — present in `ui.boundary`

```text
<<boundary>>
-opcionPago: String
-datosPago:String
+obtenerOpcionPago():String
```

### `PantallaPago` — present in `ui.boundary`

```text
<<boundary>>
-formulario: FormularioPago
-control: ComprarAlimentoManejador
+PantallaPago(): void
+mostrarDatosPago(): void
```

### `PantallaTicket` — present in `ui.boundary`

```text
<<boundary>>
-controlTicket: ControladorTicket
+PantallaTicket(): void
+mostrarTicket(): void
```

### `PantallaMesasDisponibles` — present in `ui.boundary`

```text
<<boundary>>
-mesas: List<Mesa>
-controlMesas: ReservarMesaManejador
+seleccionMesa(): Mesa
+MesasDisponibles(): void
```

### `FormularioResenia` — removed from current code with customer-service feature

```text
<<boundary>>
-visible: boolean
-mensaje: String
-tema: String
+FormularioResenia(): void
+obtenerResenia(): Resenia
```

### `PantallaServicioCliente` — removed from current code with customer-service feature

```text
<<boundary>>
-FAQ: vistaFAQ
-formResenias: FormularioResenia
-administracionFAQ: AdministradorFAQ
-gestionResenia: GestorResenias
+PantallaServicioCliente(): void
+MesasDisponibles(): void
```

### `vistaFAQ` — removed from current code with customer-service feature

```text
<<boundary>>
-visible: boolean
-FAQs: list<FAQ>
-tema: String
+FormularioResenia(): void
+obtenerResenia(): Resenia
```

The operations printed in `vistaFAQ` refer to reviews rather than FAQs; this
inconsistency is retained from the UML.

## Boundary relationships, roles, and multiplicities

In the role columns below, source and destination names are the names printed
in the UML. A field in a screen is shown as that screen's role toward its
collaborator; the inverse endpoint role is marked unspecified where the image
does not provide one. A singleton field means one reference from each screen
instance, not exclusive ownership or a global inverse cardinality.

| Source / role | Target / role | UML type / label | Multiplicity in source | Current interpretation/status |
| --- | --- | --- | --- | --- |
| `PantallaCajero.vistaPedidos` | `vistaPedidosCajero` / cashier-orders view | Form/view ownership; user-confirmed composition | 1 screen to 1 view | Present; the inverse is one owning screen per private view. |
| `PantallaCajero.controladorTickets` | `ControladorTickets` / ticket controller | Association | 1 reference; inverse unspecified | Present. |
| `PantallaCajero.controlTickets` | `ControladorTickets` / ticket controller | Association | 1 reference; inverse unspecified | Present as a second distinct-looking role. |
| `PantallaCajero.pagos` | `PagarEfectivo` / cash-payment control | Association | 1 reference; inverse unspecified | Present. |
| `PantallaCajero.controlFacturas` | `FacturaControlador` / invoice control | Association | 1 reference; inverse unspecified | Present. |
| `PantallaEstadisticas.calculador` | `CalculadorEstadisitcas` / statistics control | Association | 1 reference; inverse unspecified | Present; collaborator spelling differs from the control name elsewhere in the UML. |
| `PantallaEstadisticas.graficos` | `GeneradorGraficos` / graph control | Association | 1 reference; inverse unspecified | Present. |
| `PantallaEstadisticas.seleccionMesa()` | `Mesa` / selected statistics context | Operation return, no persistent association | At most one Mesa per call | Present as a screen-local selector. The operation returns its selected domain object; it does not add a link to `PantallaMesasDisponibles`. |
| `Login.formLoging` | `FormularioLoging` / login form | Form ownership; user-confirmed composition | 1 screen to 1 form | Present as `FormularioLogin`; UML role/type spelling differs. |
| `Login.autenticacion` | `AutenticarUsuarioControlador` / authentication control | Association | 1 reference; inverse unspecified | Present. |
| `Login.usr` | `Usuario` / authenticated user | Association | 1 reference; inverse unspecified | Present. |
| `Menu Principal.selectorVentanas` | `SelectorVentanas` / window selector | Association | 1 reference; inverse unspecified | Present. |
| `PantallaComedor.menu` | `VistaAlimentos` / food view | Form/view ownership; user-confirmed composition | 1 screen to 1 view | Present. |
| `PantallaComedor.busqueda` | `FormularioBusqueda` / search form | Form/view ownership; user-confirmed composition | 1 screen to 1 form | Present. |
| `PantallaComedor.controlAlimentos` | `SeleccionarAlimentoControlador` / food-selection control | Association | 1 reference; inverse unspecified | Present; controller name is OCR-uncertain. |
| `VistaAlimentos.menuAlimentos` | `Alimento` / displayed food | Association | 0..* foods per view (`List<Alimento>`); inverse unspecified | Present; list membership does not imply ownership. |
| `PantallaPago.formulario` | `FormularioPago` / payment form | Form/view ownership; user-confirmed composition | 1 screen to 1 form | Present. |
| `PantallaPago.control` | `ComprarAlimentoManejador` / purchase handler | Association | 1 reference; inverse unspecified | Present. |
| `PantallaMesasDisponibles.mesas` | `Mesa` / listed table | Association | 0..* tables per screen (`List<Mesa>`); inverse unspecified | Present; the diagram shows `1` labels at this screen link, but their ends are not completely clear. |
| `PantallaMesasDisponibles.controlMesas` | `ReservarMesaManejador` / reservation handler | Association | 1 reference; inverse unspecified | Present. |
| `PantallaTicket.controlTicket` | `ControladorTicket` / ticket control | Association | 1 reference; inverse unspecified | Present. |
| `PantallaComedor` / continues to | `PantallaPago` / payment screen | Navigation, label `Continua a` | `1 : 1` markers visible | Present as a screen flow; no persistent field is shown. |
| `PantallaPago` / generates or opens | `PantallaTicket` / ticket screen | Navigation, label `Genera` | `1 : 1` markers visible | Present as a screen flow; no persistent field is shown. |
| `Login` / generates | `Menu Principal` / main menu | Navigation, label `Genera` | `1 : 1` markers visible | Diagram flow only; no direct typed field in the boundary classes. |
| `Menu Principal` / permits navigation to | `PantallaComedor` / dining screen | Navigation, label `Permite ir` | `1 : 1` markers visible | The label is placed between the main-menu and dining-screen boxes. |
| `PantallaServicioCliente.FAQ` | `vistaFAQ` / FAQ view | Composition in source drawing | 1 screen to 1 view per confirmed interpretation | Removed from implementation. |
| `PantallaServicioCliente.formResenias` | `FormularioResenia` / review form | Composition in source drawing | 1 screen to 1 form per confirmed interpretation | Removed from implementation. |
| `PantallaServicioCliente.administracionFAQ` | `AdministradorFAQ` / FAQ manager | Association | 1 reference; inverse unspecified | Removed from implementation. |
| `PantallaServicioCliente.gestionResenia` | `GestorResenias` / review manager | Association | 1 reference; inverse unspecified | Removed from implementation. |

The source image places the label `Contiene` and a `1` marker next to
`VentanaError`, but its remote endpoint is obscured by overlapping connectors;
the role and multiplicity are therefore not assigned to a specific screen.
The label `Permite` and `1` markers also appear between `PantallaEstadisticas`
and `Login`; the connector's direction/meaning is unclear. These are retained
as unresolved UML notes rather than assigned to an invented typed field.

The diagram also contains standalone `FormularioLogin`, `FormularioBusqueda`,
`VistaAlimentos`, `FormularioPago`, `FormularioResenia`, and `vistaFAQ`
boundary boxes. Their operations and typed fields are copied above; no
additional ownership is inferred beyond the screen relationships listed here.

## Difference from the current source tree

The original UML inventory above deliberately preserves all boundary boxes,
including the customer-service group. The implementation has removed
`PantallaServicioCliente`, `FormularioResenia`, and `vistaFAQ`, along with
their service-specific collaborators, as requested. The remaining 15 boundary
classes are physically organized under
`src/main/java/com/proyectoingsw/cafeteria/ui/boundary/` with Java package
`com.proyectoingsw.cafeteria.ui.boundary`. Their behavior and relations are
unchanged by the package move; the corresponding relationship test is in the
matching test package.

The diagram's casing/typos and incomplete signatures above are source
transcriptions. The implementation normalizes some names/types to compile
(`Menu Principal` to `MenuPrincipal`, `formLoging` to `formulario`, and
`FormularioLoging` to `FormularioLogin`) and may supply types omitted by the
image. Screen-component links are simple associations except the specifically
confirmed 1:1 screen-owned form/view compositions. Those are design decisions
confirmed for the implementation, not a claim that every connector or label in
the embedded image is legible.

The GUI implements `PantallaEstadisticas.seleccionMesa(): Mesa` as a local
screen selection, as confirmed for this feature. The app supplies Mesa options
from its in-memory DAO to both the statistics selector and the table screen;
there is no persistent association between those two screens. The existing
`CalculadorEstadisticas` and `GeneradorGraficos` contracts do not accept a Mesa,
so the selected object is returned to callers while current report/chart
operations remain global; per-Mesa computation needs a later service-contract
change rather than invented behavior in the boundary.
