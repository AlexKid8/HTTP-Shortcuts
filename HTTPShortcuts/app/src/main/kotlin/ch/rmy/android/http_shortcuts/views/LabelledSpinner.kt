package ch.rmy.android.http_shortcuts.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.extensions.findIndex
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class LabelledSpinner @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : com.satsuware.usefulviews.LabelledSpinner(context, attrs, defStyleAttr) {

    private val selectionChangeSubject = PublishSubject.create<String>()

    val selectionChanges: Observable<String>
        get() = selectionChangeSubject

    var items: Array<String> = emptyArray()
        set(value) {
            field = value
            setItemsArray(value)
        }

    init {
        val paddingTop = context.resources.getDimensionPixelSize(R.dimen.spinner_padding_top)
        label.setPadding(0, paddingTop, 0, 0)
        errorLabel.visibility = View.GONE

        onItemChosenListener = object : com.satsuware.usefulviews.LabelledSpinner.OnItemChosenListener {
            override fun onItemChosen(labelledSpinner: View?, adapterView: AdapterView<*>?, itemView: View?, position: Int, id: Long) {
                selectedItem = items[position]
            }

            override fun onNothingChosen(labelledSpinner: View?, adapterView: AdapterView<*>?) {

            }
        }
    }

    var selectedItem: String = ""
        set(value) {
            val before = field
            field = value
            setSelection(items.findIndex(value))
            if (before != value) {
                selectionChangeSubject.onNext(value)
            }
        }

}