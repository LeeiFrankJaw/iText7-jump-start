I went through [iText 7: Jump-Start
Tutorial](https://developers.itextpdf.com/content/itext-7-jump-start-tutorial)
and created this repository as record.

In those code section related to table creation, I made some
modification using the Java 8 `Consumer` and `BiConsumer` interface
from `java.util.function` so that the boolean `isHeader` is only
checked only once, instead that it gets checked every time in the loop
body.

For chapter 5, I made a personal exercise to remove the first or last
pdf object in every pdf page, depending how the main method was
invoked.
