package com.damiancz2.personalstats

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.damiancz2.personalstats.activities.MainActivity
import com.damiancz2.personalstats.adapter.QuestionnaireAdapter
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EndToEndTest {

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity>
        = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun createNewQuestionnaireAndAddQuestion() {
        onView(withId(R.id.NewQuestionnaireButton)).perform(click())

        onView(withId(R.id.NewQuestionnaireNameInputTextBox))
            .check(matches(isDisplayed()))
        onView(withId(R.id.NewQuestionnaireSubmitButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.NewQuestionnaireNameInputTextBox))
            .perform(typeText("Test1"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.NewQuestionnaireSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()

        onView(withId(R.id.AddQuestionButton)).perform(click())

        verifyAddQuestionLayout()

        onView(withId(R.id.AddQuestionInputTextBox))
            .perform(typeText("Test question 1"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.NumberAnswerTypeRadioButton)).perform(click())
        onView(withId(R.id.AddQuestionSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()

        onView(withId(R.id.BackToMainEditQuestionnaire)).perform(click())

        onView(withId(R.id.NewQuestionnaireButton)).check(matches(isDisplayed()))
        onView(withText("Your questionnaires")).check(matches(isDisplayed()))
        onView(withId(R.id.QuestionnaireRecView)).check(matches(isDisplayed()))

        onView(withId(R.id.QuestionnaireRecView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<QuestionnaireAdapter.ViewHolder>(
                0, ClickOnChlidViewAction.clickChildViewWithId(R.id.imageViewDelete))
        )

        onView(withText("Are you sure you want to delete questionnaire Test1? It will delete all the data for this questionnaire"))
            .check(matches(isDisplayed()))
        onView(withText("Yes")).perform(click())
    }

    @Test
    fun createNewQuestionnaireAddAndAnswerQuestion() {
        onView(withId(R.id.NewQuestionnaireButton)).perform(click())

        onView(withId(R.id.NewQuestionnaireNameInputTextBox))
            .check(matches(isDisplayed()))
        onView(withId(R.id.NewQuestionnaireSubmitButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.NewQuestionnaireNameInputTextBox))
            .perform(typeText("Test2"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.NewQuestionnaireSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()

        onView(withId(R.id.AddQuestionButton)).perform(click())

        verifyAddQuestionLayout()

        onView(withId(R.id.AddQuestionInputTextBox))
            .perform(typeText("Test question 1"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.NumberAnswerTypeRadioButton)).perform(click())
        onView(withId(R.id.AddQuestionSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()

        onView(withId(R.id.BackToMainEditQuestionnaire)).perform(click())

        onView(withId(R.id.NewQuestionnaireButton)).check(matches(isDisplayed()))
        onView(withText("Your questionnaires")).check(matches(isDisplayed()))
        onView(withId(R.id.QuestionnaireRecView)).check(matches(isDisplayed()))

        onView(withId(R.id.QuestionnaireRecView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<QuestionnaireAdapter.ViewHolder>(
                0, ClickOnChlidViewAction.clickChildViewWithId(R.id.imageViewAnswer)))

        verifyAnswerQuestionLayout("Test question 1")
        onView(withId(R.id.NumberPicker)).check(matches(isDisplayed()))

        onView(withId(R.id.NumberPicker))
            .perform(typeText("123"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.SubmitButton)).perform(click())

        onView(withText("Submitted")).check(matches(isDisplayed()))
        onView(withId(R.id.SubmittedBackToMainMenu)).check(matches(isDisplayed()))

        onView(withId(R.id.SubmittedBackToMainMenu)).perform(click())

        onView(withId(R.id.QuestionnaireRecView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<QuestionnaireAdapter.ViewHolder>(
                0, ClickOnChlidViewAction.clickChildViewWithId(R.id.imageViewDelete)))

        onView(withText("Are you sure you want to delete questionnaire Test2? It will delete all the data for this questionnaire"))
            .check(matches(isDisplayed()))
        onView(withText("Yes")).perform(click())
    }

    @Test
    fun createQuestionnaireEditAddAndAnswerQuestions() {
        onView(withId(R.id.NewQuestionnaireButton)).perform(click())

        onView(withId(R.id.NewQuestionnaireNameInputTextBox))
            .check(matches(isDisplayed()))
        onView(withId(R.id.NewQuestionnaireSubmitButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.NewQuestionnaireNameInputTextBox))
            .perform(typeText("Test3"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.NewQuestionnaireSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()

        onView(withId(R.id.BackToMainEditQuestionnaire)).check(matches(isDisplayed()))
        onView(withId(R.id.BackToMainEditQuestionnaire)).perform(click())


        onView(withId(R.id.QuestionnaireRecView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<QuestionnaireAdapter.ViewHolder>(
                0, ClickOnChlidViewAction.clickChildViewWithId(R.id.imageViewEdit)))

        onView(withId(R.id.AddQuestionButton)).check(matches(isDisplayed()))
        onView(withId(R.id.AddQuestionButton)).perform(click())

        verifyAddQuestionLayout()

        onView(withId(R.id.AddQuestionInputTextBox))
            .perform(typeText("Test number question"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.NumberAnswerTypeRadioButton)).perform(click())
        onView(withId(R.id.AddQuestionSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()
        onView(withId(R.id.AddQuestionButton)).perform(click())
        verifyAddQuestionLayout()

        onView(withId(R.id.AddQuestionInputTextBox))
            .perform(typeText("Test text question"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.TextAnswerTypeRadioButton)).perform(click())
        onView(withId(R.id.AddQuestionSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()
        onView(withId(R.id.AddQuestionButton)).perform(click())
        verifyAddQuestionLayout()

        onView(withId(R.id.AddQuestionInputTextBox))
            .perform(typeText("Test yes/no question"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.YesNoAnswerTypeRadioButton)).perform(click())
        onView(withId(R.id.AddQuestionSubmitButton)).perform(click())

        verifyEditQuestionnaireLayout()

        onView(withId(R.id.BackToMainEditQuestionnaire)).perform(click())

        onView(withId(R.id.NewQuestionnaireButton)).check(matches(isDisplayed()))
        onView(withText("Your questionnaires")).check(matches(isDisplayed()))
        onView(withId(R.id.QuestionnaireRecView)).check(matches(isDisplayed()))

        onView(withId(R.id.QuestionnaireRecView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<QuestionnaireAdapter.ViewHolder>(
                0, ClickOnChlidViewAction.clickChildViewWithId(R.id.imageViewAnswer)))

        verifyAnswerQuestionLayout("Test number question")
        onView(withId(R.id.NumberPicker)).check(matches(isDisplayed()))

        onView(withId(R.id.NumberPicker))
            .perform(typeText("123"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.SubmitButton)).perform(click())

        verifyAnswerQuestionLayout("Test text question")
        onView(withId(R.id.PreviousQuestionButton)).check(matches(isDisplayed()))

        onView(withId(R.id.SkipButton)).perform(click())

        verifyAnswerQuestionLayout("Test yes/no question")
        onView(withId(R.id.PreviousQuestionButton)).check(matches(isDisplayed()))
        onView(withId(R.id.PreviousQuestionButton)).perform(click())

        verifyAnswerQuestionLayout("Test text question")

        onView(withId(R.id.TextPicker)).check(matches(isDisplayed()))
        onView(withId(R.id.TextPicker))
            .perform(typeText("Random text"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.SubmitButton)).perform(click())

        verifyAnswerQuestionLayout("Test yes/no question")
        onView(withId(R.id.yesNoRadioGroup)).check(matches(isDisplayed()))
        onView(withId(R.id.yesRadioButton)).check(matches(isDisplayed()))
        onView(withId(R.id.noRadioButton)).check(matches(isDisplayed()))

        onView(withId(R.id.yesRadioButton)).perform(click())
        onView(withId(R.id.SubmitButton)).perform(click())

        onView(withText("Submitted")).check(matches(isDisplayed()))
        onView(withId(R.id.SubmittedBackToMainMenu)).check(matches(isDisplayed()))

        onView(withId(R.id.SubmittedBackToMainMenu)).perform(click())

        onView(withId(R.id.QuestionnaireRecView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<QuestionnaireAdapter.ViewHolder>(
                0, ClickOnChlidViewAction.clickChildViewWithId(R.id.imageViewDelete)))

        onView(withText("Are you sure you want to delete questionnaire Test3? It will delete all the data for this questionnaire"))
            .check(matches(isDisplayed()))
        onView(withText("Yes")).perform(click())
    }

    private fun verifyAnswerQuestionLayout(question: String) {
        onView(withText(question)).check(matches(isDisplayed()))
        onView(withText("Submit")).check(matches(isDisplayed()))
        onView(withText("Skip")).check(matches(isDisplayed()))
        onView(withText("Delete")).check(matches(isDisplayed()))
        onView(withId(R.id.SubmitButton)).check(matches(isDisplayed()))
        onView(withId(R.id.SkipButton)).check(matches(isDisplayed()))
        onView(withId(R.id.DeleteButton)).check(matches(isDisplayed()))
        onView(withId(R.id.QuestionTextView)).check(matches(isDisplayed()))
    }

    private fun verifyEditQuestionnaireLayout() {
        onView(withId(R.id.EditQuestionnaireNameTextBox)).check(matches(isDisplayed()))
        onView(withId(R.id.AddQuestionButton)).check(matches(isDisplayed()))
        onView(withId(R.id.btnEditQuestionnaireName)).check(matches(isDisplayed()))
        onView(withId(R.id.BackToMainEditQuestionnaire)).check(matches(isDisplayed()))
    }

    private fun verifyAddQuestionLayout() {
        onView(withId(R.id.AddQuestionInputTextBox)).check(matches(isDisplayed()))
        onView(withId(R.id.AddQuestionSubmitButton)).check(matches(isDisplayed()))
        onView(withId(R.id.AnswerTypeTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.AnswerTypeRadioGroup)).check(matches(isDisplayed()))
        onView(withId(R.id.NumberAnswerTypeRadioButton)).check(matches(isDisplayed()))
        onView(withId(R.id.TextAnswerTypeRadioButton)).check(matches(isDisplayed()))
        onView(withId(R.id.TimeAnswerTypeRadioButton)).check(matches(isDisplayed()))
        onView(withId(R.id.YesNoAnswerTypeRadioButton)).check(matches(isDisplayed()))
    }

    object ClickOnChlidViewAction {
        fun clickChildViewWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController?, view: View) {
                    val v: View = view.findViewById(id)
                    v.performClick()
                }
            }
        }
    }
}