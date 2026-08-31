# MAGIC.md — ae3.sys.pkg.l2.tgt.pdf

## For keeper-ae3 / magic-tester

**`?___output=pdf` on a real `"grid"` layout fails with `IllegalStateException: The document has no
content!`, 2026-08-26 — found live-verifying `MakeDataViewReplyFn.js`'s Phase 0 (full detail in
`ae3-interfaces.backlog.md`'s own `Context Facts`).** Dispatch is confirmed correct — the request
really reaches `WebContextPdf`/`PdfTargetContext`, real `javac`-compiled code, not a fallback path
(the 500's own stack trace names `PdfTargetContext.defaultDocumentDestroy`/`doFinish` directly). The
walk itself completes with no exception; the failure only surfaces at close-time, when
`defaultDocumentDestroy` finds `document.getPageNumber() == 0`.

Traced as far as static reading allows without instrumenting this file (out of Phase 0's own
authorized scope): `PdfTargetContext.onNest` calls `this.document.newPage()` as soon as a non-empty
`title` is seen on the root layout — which fires immediately, before any real content has been added
to the still-fresh document. `com.lowagie.text.Document.newPage()` is documented to no-op (does not
advance the page count) when called on an already-empty page — ordinary, expected behavior for that
call in isolation. The grid's own content (`PdfLayoutGrid`/`PdfElementGrid`, walked via
`context.setSequence`) traces correctly on paper — cells added via `addElement`, the finished table
flushed to the document via `doFinishAndGetParent`'s `this.parent.addElement(context, this)` — so
why the document still reports zero pages once that table is added was not pinned down further; a
real, previously-unexercised interaction (no live test of a top-level `"grid"` layout through this
code path existed before this pass), not confirmed as one specific one-line bug. Not fixed this
pass — a stop-and-check-back item, not touched.
