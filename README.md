# trabajoDDD
Trabajo sobre la implementación de un sistema de arquitectura DDD con interfaces predefinidas que se deben aplicar 
Creado por: Hugo de Pablo y Lord Programador
Almacenamiento = JSON
Lider: Hugo de Pablo
Subdito: Puto pringado Rubén Martín Andrade

# Reglas para la creación del código(Obligatorias)
---

# Buenas Prácticas de Código (Revisadas y Ampliadas)

## I. Nombres y Estructura

1.  **Nombres Descriptivos y Consistentes:**
    * Utiliza nombres completos y claros para variables, funciones, clases y métodos, reflejando su propósito.
    * Adopta una convención de nomenclatura (por ejemplo, `camelCase`, `PascalCase`, `snake_case`) y aplícala consistentemente en todo el proyecto.

2.  **Declaración Mínima de Variables:**
    * Declara variables solo cuando sean necesarias y tan cerca de su primer uso como sea posible.
    * Elimina siempre las variables declaradas que no se utilicen.

3.  **Evitar Números Mágicos (Constantes):**
    * Reemplaza los valores numéricos sin explicación inmediata (Números Mágicos) por constantes bien nombradas (Ej: `MAX_USUARIOS`, `TIMEOUT_SEGUNDOS`).
    * Si un número debe usarse directamente, su propósito debe ser claramente justificado con un comentario en esa línea.

## II. Funciones y Clases

4.  **Funciones Cortas y Enfocadas (SRP):**
    * Las funciones deben ser cortas (máximo aproximadamente 20 líneas de lógica) y seguir el Principio de Responsabilidad Única (SRP): cada función debe hacer una única cosa bien definida.

5.  **Parámetros Mínimos de Funciones:**
    * Limita las funciones a un máximo de tres argumentos (idealmente dos o menos) para mantener la interfaz simple.
    * Si necesitas más, considera refactorizar la función, usar un objeto de configuración (`DTO`/`VO`), o agrupar parámetros relacionados.

6.  **Clases Cohesivas y Pequeñas:**
    * Las clases deben ser cohesivas y tener un tamaño manejable (evitar que excedan las 300-500 líneas). Si una clase es muy larga, divídela en varias más pequeñas y enfocadas.

7.  **Encapsulamiento Estricto:**
    * Respeta el encapsulamiento utilizando modificadores de acceso (`private`, `protected`) para ocultar los detalles internos.
    * Expón solo la interfaz pública necesaria. Accede a los miembros internos a través de métodos que realicen acciones, en lugar de *getters* y *setters* si es posible.

## III. Documentación y Estilo

8.  **Comentarios Explicativos:**
    * Comenta el código para explicar **el porqué** (la intención, la lógica compleja, las suposiciones), no solo **el qué** (que debe ser obvio por el código).
    * Documenta la interfaz pública (funciones, clases) con detalles de parámetros, retorno y posibles excepciones.

9.  **Comentarios Relevantes y Útiles:**
    * Asegúrate de que todos los comentarios sean concisos, precisos y aporten valor.
    * Elimina comentarios obsoletos, triviales o que no añadan información.

10. **Ortografía, Gramática y Formato:**
    * Revisa siempre la ortografía y gramática en nombres y comentarios.
    * Mantén un formato de código consistente (indentación, espacios, saltos de línea) utilizando herramientas de *linting* o formateo automático si es posible.

## IV. Principios Generales

11. **Principio DRY (Don't Repeat Yourself):**
    * Evita la duplicación de código. Si ves el mismo fragmento de código más de una vez, extráelo en una función, método o clase reutilizable.

12. **Manejo Explícito de Errores:**
    * Nunca ignores errores o excepciones. Utiliza mecanismos apropiados (excepciones, códigos de error, etc.) para que el flujo de control y las posibles fallas sean claros y predecibles.

13. **Código de Mínima Sorpresa:**
    * El código debe comportarse de la manera que un desarrollador razonable esperaría. Evita efectos secundarios inesperados o funciones que realicen acciones no sugeridas por su nombre.

14. **Uso de Librerías Estándar:**
    * Prioriza el uso de la biblioteca estándar del lenguaje y de librerías de terceros probadas sobre la implementación de tu propia lógica compleja (Ej: manejo de fechas, algoritmos).

---

