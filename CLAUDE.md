# CLAUDE.md — ae3.sys.pkg.l2.tgt.pdf

AE3 L2 media target for PDF output, built directly on the old iText fork (`com.lowagie.text.*` package names — pre-`com.itextpdf` rename, so this is the pre-2.1.7/LGPL-era iText, not a modern one).

## Structure

- `java/ru/myx/ae3/l2/pdf/`
  - `PdfTargetContext extends TargetContextAbstract<PdfTargetContext>` (abstract) — the base target-context.
  - Three output variants: `PdfTargetFileContext`, `PdfTargetFragmentContext`, `PdfTargetReplyContext` (all `extends PdfTargetContext`) — file/fragment-composition/direct-reply, more variants than the simpler `l2.tgt.*` targets (`json`, `text`) have.
  - `PdfLayoutDefinition` (abstract, package-private) `implements LayoutDefinitionAbstract<PdfTargetContext>, ContextHandler<PdfTargetContext, BaseObject>` — base for the registered layouts (`Container`, `Link`, `Image`, `Numbered`, `Grid`, `String`, `SequenceAttachmentWest`, `Sequence`).
  - `PdfElement*` classes (`PdfElementDocument`, `PdfElementNumbered`, `PdfElementNumberedTable`, `PdfElementGrid`, `PdfElementParagraph`, `PdfElementSequence`, `PdfElementSequenceAttachmentWest`) directly `extends` iText classes (`Document`, `List`, `PdfPTable`, `Paragraph`) and `implements` a local `PdfElement` marker interface — iText integration is structural (subclassing), not incidental.
  - `ru/myx/util/pdf/` — `PdfAPI`, `DocumentBuilder`: lower-level iText helpers, separate from the `ru.myx.ae3.l2.pdf` target-context layer above.

## Build

- Requires (Java, AE3-internal, confirmed by source imports): `ae3.sdk` — cross-package imports resolve to `ru.myx.ae3.answer.*`/`ru.myx.ae3.i3.TargetInterface`/`ru.myx.ae3.serve.*` (`ae3.api`) or `ru.myx.ae3.l2.*`/`ru.myx.ae3.binary.*` (`ae3.sdk`).
- **Also requires the iText library (`com.lowagie.text.*`) — genuinely unresolved.** No bundled `.jar` or `lib/` folder exists anywhere in this repo, and none of the 11 real `project.inf`/`.classpath` files read across this workspace show a pattern for declaring an external (non-AE3) Java library dependency — every other unit's dependencies are all AE3-internal. `.classpath` has a comment flagging this rather than a guessed jar path; `project.inf` doesn't attempt to declare it at all. Don't add one without confirming how this build system actually resolves third-party jars.
- `package.json`: two bundles — `ae3.sys.l2.tgt.pdf` (main) and `ae3.sys.l2.tgt.pdf-docs` (`"requires": ["ae3.web", "ae3.manual"]` — a documentation-specific sub-bundle, not reflected in this unit's Java `project.inf` `Requires:`).

## Gotchas

- **This unit had no `.gitignore`/`.project`/`.classpath`/`project.inf` at all** when checked out this session — added fresh, modeled on `ae3.sys.pkg.l2.tgt.html`'s versions.
- **`.gitignore`'s `*.jar` rule may be wrong for this unit specifically** — inherited unchanged from the `l2.tgt.xml` template, which has no jar dependencies of its own. If iText ever gets vendored into this repo (e.g. a `lib/` folder), a blanket `*.jar` ignore would silently exclude it from git. Left as-is since it's unknown whether jars are meant to be vendored here at all vs. resolved externally — reconsider once the iText dependency question above is actually resolved.
- **26 compiled `.class` files are checked into git under `bin/`** — not yet cleaned up.
- See `ae3.sys.pkg.l2.tgt.html`'s CLAUDE.md for the broader discussion of why `l2.tgt.xml`'s three-entry `Provides:` shape isn't treated as a confirmed convention here.
