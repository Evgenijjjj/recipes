package r.evgenymotorin.recipes.parsing

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import r.evgenymotorin.recipes.model.About
import r.evgenymotorin.recipes.model.Post


const val PARSING_LOG = "parsing_log"

class Parsers {

    private val defaultPictureUrl = "https://e2.edimdoma.ru/assets/default/recipes/ed4_small-3f094b81f7380fec619ddffd4c31b83d.png"

    fun findPosts(url: String): List<Element>? {
        var doc: Document
        val list = ArrayList<Element>()

        try {

            doc = Jsoup.connect(url)
                .get()
            val postsObject: Elements = doc.select(
                "article[class=card]"
            )

            for (post in postsObject)
                list.add(post)

        } catch (e: Exception) {
            Log.d(PARSING_LOG, e.toString())
            return null
        }

        return list
    }

    fun scrapPostFromHTML(post: Element): Post {

        var imgUrl = post.select("img[title]")
            .attr("src")

        if (imgUrl == defaultPictureUrl)
            imgUrl = ""

        val recipeName = post.select("img[title]")
            .attr("title")

        val postUrl = "https://www.edimdoma.ru" + post
            .select("a[href]")[0]
            .attr("href")

        var countOfPersons = ""
        var cookingTime = ""
        var ingredientsCount = ""

        val recipePageHTML = Jsoup.connect(postUrl).get()

        if (recipePageHTML.getElementsByClass("entry-stats__item entry-stats__item_persons") != null)
            countOfPersons = recipePageHTML.select("div[class=entry-stats__item entry-stats__item_persons]")
                .select("div[class=entry-stats__value]")
                .text()


        if (recipePageHTML.getElementsByClass("entry-stats__item entry-stats__item_cooking") != null)
            cookingTime = recipePageHTML.select("div[class=entry-stats__item entry-stats__item_cooking]")
                .select("div[class=entry-stats__value]")
                .text()

        if (recipePageHTML.getElementsByClass("field-row recipe_ingredients") != null) {
            ingredientsCount = recipePageHTML.select("div[class=field-row recipe_ingredients]")
                .select("table[class=definition-list-table]").size.toString()
            Log.d(PARSING_LOG+ "dfegrbh", "carusel size  = $ingredientsCount for: $postUrl")
        }

        else if (recipePageHTML.getElementsByClass("content-box__content content-box__content_grey js-mediator-article") != null) {
            ingredientsCount = "${recipePageHTML
                .select("div[class=content-box__content content-box__content_grey js-mediator-article]")
                .select("br").size + 1}"
            Log.d(PARSING_LOG+ "dfegrbh", "NOT CARUSEL s: $ingredientsCount FOR $postUrl")
        }


        if (recipePageHTML.select("table[class=definition-list-table]").isNotEmpty())
            Log.d(PARSING_LOG, "url: $postUrl")


        return Post(postUrl, imgUrl, recipeName, countOfPersons, cookingTime, ingredientsCount)
    }

    fun scrapAboutInformationFromHTML(page: Document): About {
        val list = ArrayList<String>()

        val description = page.select("div[class=recipe_description]").text()

        if (page.select("div[class=thumb-slider__slide]") != null && !page.select("div[class=thumb-slider__slide]").toString().isNullOrEmpty()) {
            Log.d(PARSING_LOG + "....", "carusel = ${page.select("div[class=thumb-slider__slide]")}")
            for (e in page.select("div[class=thumb-slider__slide]"))
                list.add(e.select("img").attr("src"))
        } else {
            val link = page.select("div[class=content-media]").select("img").attr("src")

            if (link != defaultPictureUrl)
                list.add(link)

            Log.d(PARSING_LOG + "....", "1 photo :link = ${link} ")
        }


        return About(list, description)
    }
}