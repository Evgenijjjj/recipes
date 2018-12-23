package r.evgenymotorin.recipes.parsing

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import r.evgenymotorin.recipes.model.*


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

    fun scrapIngredientsFromHTML(page: Document): Ingredients? {
        if (!page.select("div[class=field-row recipe_ingredients]").toString().isNullOrEmpty()) {
            var title = ""

            title = page.select("div[class=field-row field-row_mb20 field-row_portion-value]")
                .select("div[class=title title_sans-serif title_small title_uppercase]")
                .text()

            title += " " + page.select("div[class=field-row field-row_mb20 field-row_portion-value]")
                .select("input[class=field__input]")
                .attr("value") + " "

            title += page.select("div[class=field-row field-row_mb20 field-row_portion-value]")
                .select("div[class=title title_sans-serif title_small title_uppercase portion-pluralized]")
                .text()

            val ingredients = page
                .select("div[class=content-box__content content-box__content_grey js-mediator-article]")
                .select("table[class=definition-list-table]")

            val ingredientsList: ArrayList<AdaptiveIngredient> = arrayListOf()

            for (i in ingredients) {
                val name = i.select("span[class=recipe_ingredient_title]")
                    .text()

                if (name.isEmpty()) continue

                val count = i.select("td[class=definition-list-table__td definition-list-table__td_value]")
                    .text()

                var about: String? = null

                Log.d(PARSING_LOG + "sdafsgd...", "about: ${i.select("div[class=checkbox-info-container]").text()}")

                if (!i.select("div[class=checkbox-info-container]").toString().isNullOrEmpty())
                    about = i.select("div[class=checkbox-info-container]")
                        .select("div[class=checkbox-info__description]")
                        .text()

                ingredientsList.add(AdaptiveIngredient(name, count, about))
                Log.d(PARSING_LOG + "sdafsgd...", "ingredient: $name, count: $count")
            }


            Log.d(PARSING_LOG + "sdafsgd", "title: $title")
            return Ingredients(true, title, ingredientsList, null)

        } else if (page.select("div[class=section-title title]") != null) {
            var text = page.select("div[class=content-box__content content-box__content_grey js-mediator-article]")
                .select("p").toString()

            text = text.replace("<p>", "")
            text = text.replace("</p>", "")
            text = text.replace("<br>", "\n")

            Log.d(PARSING_LOG + "sdafsgd", "isNotAdaptive: $text")
            return Ingredients(false, null, null, text)
        } else
            return null
    }

    fun scrapStepsFromHTML(page: Document): ArrayList<Step> {
        val list: ArrayList<Step> = arrayListOf()

        val steps = page.select("div[class=content-box recipe_step]")

        for (s in steps) {
            var imgUrl: String? = null
            var description = ""

            if (!s.select("div[class^=step-image-container]").toString().isNullOrEmpty()) {
                imgUrl = "https://www.edimdoma.ru" + s.select("div[class^=step-image-container]")
                    .select("img[data-original]")
                    .attr("data-original")
            }

            description = s.select("div[class=plain-text recipe_step_text]")
                .text()

            list.add(Step(imgUrl, description))

            Log.d(PARSING_LOG + "steps", "img: $imgUrl, description: $description")
        }

        return list
    }
}