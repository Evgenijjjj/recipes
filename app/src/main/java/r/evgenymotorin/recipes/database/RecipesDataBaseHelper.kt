package r.evgenymotorin.recipes.database

import r.evgenymotorin.recipes.database.interfaces.*
import r.evgenymotorin.recipes.database.tables.AboutImageData
import r.evgenymotorin.recipes.database.tables.AdaptiveIngredientData
import r.evgenymotorin.recipes.database.tables.StepData
import r.evgenymotorin.recipes.db.tables.RecipeData

class RecipesDataBaseHelper {
    val db: RecipesDataBase

    private val recipeDataDao: RecipeDataDao
    private val aboutImageDataDao: AboutImageDataDao
    private val stepDataDao: StepDataDao
    private val adaptiveIngredientDataDao: AdaptiveIngredientDataDao

    constructor(recipesDataBase: RecipesDataBase) {
        this.db = recipesDataBase

        recipeDataDao = db.RecipeDataDao()
        aboutImageDataDao = db.AboutImageDataDao()
        stepDataDao = db.StepDataDao()
        adaptiveIngredientDataDao = db.AdaptiveIngredientDataDao()
    }

    fun addStepDataInRecipeData(sd: StepData, recipeData: RecipeData) {

        stepDataDao.insert(sd)
        val stepData = stepDataDao.getLastStepData()

        if (recipeData.firstStepPtr != null) {
            var stepDatePtr = stepDataDao.getStepDataWithId(recipeData.firstStepPtr!!)

            while (stepDatePtr.nextStepDataPtr != null)
                stepDatePtr = stepDataDao.getStepDataWithId(stepDatePtr.nextStepDataPtr!!)

            stepDatePtr.nextStepDataPtr = stepData.id
            stepDataDao.updateStepData(stepDatePtr)
        } else {
            recipeData.firstStepPtr = stepData.id
            recipeDataDao.updateRecipe(recipeData)
        }
    }

    fun addAboutImageDataInRecipeData(aid: AboutImageData, recipeData: RecipeData) {

        aboutImageDataDao.insert(aid)
        val aboutImageData = aboutImageDataDao.getLastAboutImageData()

        if (recipeData.firstImgAboutPtr != null) {
            var aboutImageDataPtr = aboutImageDataDao.getAboutImageDataWithId(recipeData.firstImgAboutPtr!!)

            while (aboutImageDataPtr.nextImageDataPtr != null)
                aboutImageDataPtr = aboutImageDataDao.getAboutImageDataWithId(aboutImageDataPtr.nextImageDataPtr!!)

            aboutImageDataPtr.nextImageDataPtr = aboutImageData.id
            aboutImageDataDao.updateAboutImageData(aboutImageDataPtr)
        } else {
            recipeData.firstImgAboutPtr = aboutImageData.id
            recipeDataDao.updateRecipe(recipeData)
        }
    }

    fun addAdaptiveIngredientDataInRecipeData(aid: AdaptiveIngredientData, recipeData: RecipeData) {

        adaptiveIngredientDataDao.insert(aid)
        val adaptiveIngredientData = adaptiveIngredientDataDao.getLastAdaptiveIngredientData()

        if (recipeData.firstAdaptiveIngredientPtr != null) {
            var adaptiveIngredientDataPtr = adaptiveIngredientDataDao.getAdaptiveIngredientDataWithId(recipeData.firstAdaptiveIngredientPtr!!)

            while (adaptiveIngredientDataPtr.nextAdaptiveIngredientPtr != null)
                adaptiveIngredientDataPtr = adaptiveIngredientDataDao.getAdaptiveIngredientDataWithId(adaptiveIngredientDataPtr.nextAdaptiveIngredientPtr!!)

            adaptiveIngredientDataPtr.nextAdaptiveIngredientPtr = adaptiveIngredientData.id
            adaptiveIngredientDataDao.updateAdaptiveIngredientData(adaptiveIngredientDataPtr)
        } else {
            recipeData.firstAdaptiveIngredientPtr = adaptiveIngredientData.id
            recipeDataDao.updateRecipe(recipeData)
        }
    }

    fun getAllAdaptiveIngredientsData(recipeData: RecipeData): List<AdaptiveIngredientData>? {
        if (recipeData.firstAdaptiveIngredientPtr == null) return null

        val list = ArrayList<AdaptiveIngredientData>()

        var adaptiveIngredientDataPtr = adaptiveIngredientDataDao.getAdaptiveIngredientDataWithId(recipeData.firstAdaptiveIngredientPtr!!)
        list.add(adaptiveIngredientDataPtr)

        while (adaptiveIngredientDataPtr.nextAdaptiveIngredientPtr != null) {
            adaptiveIngredientDataPtr = adaptiveIngredientDataDao.getAdaptiveIngredientDataWithId(adaptiveIngredientDataPtr.nextAdaptiveIngredientPtr!!)
            list.add(adaptiveIngredientDataPtr)
        }

        return list
    }

    fun getAllStepsForRecipeData(recipeData: RecipeData): List<StepData>? {
        if (recipeData.firstStepPtr == null) return null

        val list = ArrayList<StepData>()

        var stepDataPtr = stepDataDao.getStepDataWithId(recipeData.firstStepPtr!!)
        list.add(stepDataPtr)

        while (stepDataPtr.nextStepDataPtr != null) {
            stepDataPtr = stepDataDao.getStepDataWithId(stepDataPtr.nextStepDataPtr!!)
            list.add(stepDataPtr)
        }

        return list
    }

    fun getAllAboutImagesForRecipeData(recipeData: RecipeData): List<AboutImageData>? {
        if (recipeData.firstImgAboutPtr == null) return null

        val list = ArrayList<AboutImageData>()

        var aboutImageDataPtr = aboutImageDataDao.getAboutImageDataWithId(recipeData.firstImgAboutPtr!!)
        list.add(aboutImageDataPtr)

        while (aboutImageDataPtr.nextImageDataPtr != null) {
            aboutImageDataPtr = aboutImageDataDao.getAboutImageDataWithId(aboutImageDataPtr.nextImageDataPtr!!)
            list.add(aboutImageDataPtr)
        }

        return list
    }

    fun removeRecipeDataFromDataBase(recipeData: RecipeData) {
        val aboutList = this.getAllAboutImagesForRecipeData(recipeData)
        val stepsList = this.getAllStepsForRecipeData(recipeData)
        val adaptiveIngredientList = this.getAllAdaptiveIngredientsData(recipeData)

        if (aboutList != null)
            for (img in aboutList)
                aboutImageDataDao.deleteAboutImageDataWithId(img.id!!)

        if (stepsList != null)
            for (step in stepsList)
                stepDataDao.deleteStepDataWithId(step.id!!)

        if (adaptiveIngredientList != null)
            for (i in adaptiveIngredientList)
                adaptiveIngredientDataDao.deleteAdaptiveIngredientDataWithId(i.id!!)

        recipeDataDao.deleteRecipeWithId(recipeData.id!!)
    }
}