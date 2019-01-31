package r.evgenymotorin.recipes.parsing

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.database.tables.AboutImageData
import r.evgenymotorin.recipes.database.tables.AdaptiveIngredientData
import r.evgenymotorin.recipes.database.tables.StepData
import r.evgenymotorin.recipes.db.tables.RecipeData
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

    fun scrapPostFromHTML(post: Element): RecipeData {
        val recipeData = RecipeData()

        recipeData.imageUrl = post.select("img[title]")
            .attr("src")

        if (recipeData.imageUrl == defaultPictureUrl)
            recipeData.imageUrl = null

        recipeData.recipeName = post.select("img[title]")
            .attr("title")

        recipeData.recipeUrl = "https://www.edimdoma.ru" + post
            .select("a[href]")[0]
            .attr("href")

        val recipePageHTML = Jsoup.connect(recipeData.recipeUrl).get()

        if (recipePageHTML.getElementsByClass("entry-stats__item entry-stats__item_persons") != null)
            recipeData.numberOfServings = recipePageHTML.select("div[class=entry-stats__item entry-stats__item_persons]")
                .select("div[class=entry-stats__value]")
                .text()


        if (recipePageHTML.getElementsByClass("entry-stats__item entry-stats__item_cooking") != null)
            recipeData.cookingTime = recipePageHTML.select("div[class=entry-stats__item entry-stats__item_cooking]")
                .select("div[class=entry-stats__value]")
                .text()

        if (recipePageHTML.getElementsByClass("field-row recipe_ingredients") != null) {
            recipeData.ingredientsCount = recipePageHTML.select("div[class=field-row recipe_ingredients]")
                .select("table[class=definition-list-table]").size.toString()
            //Log.d(PARSING_LOG+ "dfegrbh", "carusel size  = $ingredientsCount for: $postUrl")
        }

        else if (recipePageHTML.getElementsByClass("content-box__content content-box__content_grey js-mediator-article") != null) {
            recipeData.ingredientsCount = "${recipePageHTML
                .select("div[class=content-box__content content-box__content_grey js-mediator-article]")
                .select("br").size + 1}"
            //Log.d(PARSING_LOG+ "dfegrbh", "NOT CARUSEL s: $ingredientsCount FOR $postUrl")
        }


       // if (recipePageHTML.select("table[class=definition-list-table]").isNotEmpty())
            //Log.d(PARSING_LOG, "url: $postUrl")

        //dbHelper.db.RecipeDataDao().insert(recipeData)
        //val newRecipeData = dbHelper.db.RecipeDataDao().getLastRecipe()

        /*val about = this.scrapAboutInformationFromHTML(recipePageHTML)
        val steps = this.scrapStepsFromHTML(recipePageHTML)
        val ingredients = this.scrapIngredientsFromHTML(recipePageHTML)

        if (about != null) {
            recipeData.aboutDescription = about.description

            for (i in about.imgUrlsList) {
                val aboutImageData = AboutImageData()
                aboutImageData.imageUrl = i
                dbHelper.addAboutImageDataInRecipeData(aboutImageData, recipeData)
            }
        }

        for (step in steps) {
            val stepData = StepData()

            stepData.stepDescription = step.description
            stepData.stepImageUrl = step.imgUrl

            dbHelper.addStepDataInRecipeData(stepData, recipeData)
        }

        if (ingredients != null) {
            if (ingredients.isAdaptive) {
                recipeData.adaptiveIngredientFlag = 1
                recipeData.adaptiveIngredientsTitle = ingredients.title

                for (adaptiveIngredient in ingredients.adaptiveIngredients!!) {
                    val adaptiveIngredientData = AdaptiveIngredientData()

                    adaptiveIngredientData.count = adaptiveIngredient.count
                    adaptiveIngredientData.description = adaptiveIngredient.aboutIngredient
                    adaptiveIngredientData.ingredient = adaptiveIngredient.ingredient

                    dbHelper.addAdaptiveIngredientDataInRecipeData(adaptiveIngredientData, recipeData)
                }

            } else {
                recipeData.adaptiveIngredientFlag = 0
                recipeData.notAdaptiveIngredientsText = ingredients.text
            }
            //recipeData.adaptiveIngredientFlag = if (ingredients.isAdaptive) 1 else 0
            //ingredients.adaptiveIngredients!![0].
        }*/

        //dbHelper.db.RecipeDataDao().updateRecipe(recipeData)
        return recipeData
        //return Post(postUrl, imgUrl, recipeName, countOfPersons, cookingTime, ingredientsCount)
    }

    fun scrapDetailedInformationForRecipeData(dbHelper: RecipesDataBaseHelper, recipeData: RecipeData) {
        val recipePageHTML = Jsoup.connect(recipeData.recipeUrl).get()

        val about = this.scrapAboutInformationFromHTML(recipePageHTML)
        val steps = this.scrapStepsFromHTML(recipePageHTML)
        val ingredients = this.scrapIngredientsFromHTML(recipePageHTML)

        if (about != null) {
            recipeData.aboutDescription = about.description

            for (i in about.imgUrlsList) {
                val aboutImageData = AboutImageData()
                aboutImageData.imageUrl = i
                dbHelper.addAboutImageDataInRecipeData(aboutImageData, recipeData)
            }
        }

        for (step in steps) {
            val stepData = StepData()

            stepData.stepDescription = step.description
            stepData.stepImageUrl = step.imgUrl

            dbHelper.addStepDataInRecipeData(stepData, recipeData)
        }

        if (ingredients != null) {
            if (ingredients.isAdaptive) {
                recipeData.adaptiveIngredientFlag = 1
                recipeData.adaptiveIngredientsTitle = ingredients.title

                for (adaptiveIngredient in ingredients.adaptiveIngredients!!) {
                    val adaptiveIngredientData = AdaptiveIngredientData()

                    adaptiveIngredientData.count = adaptiveIngredient.count
                    adaptiveIngredientData.description = adaptiveIngredient.aboutIngredient
                    adaptiveIngredientData.ingredient = adaptiveIngredient.ingredient

                    dbHelper.addAdaptiveIngredientDataInRecipeData(adaptiveIngredientData, recipeData)
                }

            } else {
                recipeData.adaptiveIngredientFlag = 0
                recipeData.notAdaptiveIngredientsText = ingredients.text
            }
        }
        dbHelper.db.RecipeDataDao().updateRecipe(recipeData)
    }

    fun scrapAboutInformationFromHTML(page: Document): About? {
        val list = ArrayList<String>()

        val description = page.select("div[class=recipe_description]").text()

        if (page.select("div[class=thumb-slider__slide]") != null && !page.select("div[class=thumb-slider__slide]").toString().isNullOrEmpty()) {
            Log.d(PARSING_LOG + "....", "carusel = ${page.select("div[class=thumb-slider__slide]")}")
            for (e in page.select("div[class=thumb-slider__slide]"))
                list.add(e.select("img").attr("src"))
        } else {
            val link = page.select("div[class=content-media]").select("img").attr("src")

            if (link != defaultPictureUrl && link.isNotEmpty())
                list.add(link)

            Log.d(PARSING_LOG + "....", "1 photo :link = ${link} ")
        }

        if (list.isEmpty() && description.isNullOrEmpty()) return null
        return About(list, description)
    }

    private fun scrapIngredientsFromHTML(page: Document): Ingredients? {
        if (!page.select("div[class=field-row recipe_ingredients]").toString().isEmpty()) {
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

                if (!i.select("div[class=checkbox-info-container]").toString().isEmpty())
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

    private fun scrapStepsFromHTML(page: Document): ArrayList<Step> {
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

    fun scrapAndSaveRecipeDataFromHTML(dbHelper: RecipesDataBaseHelper, recipeUrl: String, recipePage: Document): RecipeData? {
        val steps = this.scrapStepsFromHTML(recipePage)
        val about = this.scrapAboutInformationFromHTML(recipePage)
        val ingredients = this.scrapIngredientsFromHTML(recipePage)

        if (steps.count() < 1 || ingredients == null) return null

        val recipeData = RecipeData()

        recipeData.recipeUrl = recipeUrl
        recipeData.recipeName = recipePage.select("h1[class^=recipe-header__name]").text()
        recipeData.imageUrl = about?.imgUrlsList?.get(0)

        recipeData.numberOfServings = if (recipePage.getElementsByClass("entry-stats__item entry-stats__item_persons") != null) {
            recipePage.select("div[class=entry-stats__item entry-stats__item_persons]")
                .select("div[class=entry-stats__value]")
                .text()
        } else ""

        recipeData.cookingTime = if (recipePage.getElementsByClass("entry-stats__item entry-stats__item_cooking") != null) {
            recipePage.select("div[class=entry-stats__item entry-stats__item_cooking]")
                .select("div[class=entry-stats__value]")
                .text()
        } else ""

        recipeData.ingredientsCount = if (recipePage.getElementsByClass("field-row recipe_ingredients") != null) {
            recipePage.select("div[class=field-row recipe_ingredients]")
                .select("table[class=definition-list-table]").size.toString()
        }
        else if (recipePage.getElementsByClass("content-box__content content-box__content_grey js-mediator-article") != null) {
            "${recipePage
                .select("div[class=content-box__content content-box__content_grey js-mediator-article]")
                .select("br").size + 1}"
        } else ""

        dbHelper.db.RecipeDataDao().insert(recipeData)
        val newRecipeData = dbHelper.db.RecipeDataDao().getLastRecipe()

        for (step in steps) {
            val stepData = StepData()

            stepData.stepDescription = step.description
            stepData.stepImageUrl = step.imgUrl

            dbHelper.addStepDataInRecipeData(stepData, newRecipeData)
        }

        for (image in about!!.imgUrlsList) {
            val aboutImageData = AboutImageData()

            aboutImageData.imageUrl = image
            dbHelper.addAboutImageDataInRecipeData(aboutImageData, newRecipeData)
        }

        Log.d(PARSING_LOG + "db", "$newRecipeData")

        return newRecipeData
    }
}