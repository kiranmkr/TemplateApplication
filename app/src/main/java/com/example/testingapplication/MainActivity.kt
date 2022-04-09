package com.example.testingapplication

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.testingapplication.datamodel.ImageView
import com.example.testingapplication.datamodel.Root
import com.example.testingapplication.datamodel.TextView
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    var imageViewArray: ArrayList<ImageView> = ArrayList()
    var textViewJson: ArrayList<TextView> = ArrayList()
    var rootLayout: RelativeLayout? = null
    var screenRatioFactor: Double = 1.0
    var screenWidth: Double = 720.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.root_layout)

        val viewTreeObserver = rootLayout?.viewTreeObserver
        viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun onGlobalLayout() {
                rootLayout?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                if (rootLayout != null) {
                    screenWidth = rootLayout!!.width.toDouble()
                }

                try {

                    if (loadJSONFromAsset() != null) {

                        val obj = JSONObject(loadJSONFromAsset()!!)
                        val om = ObjectMapper()
                        val root: Root = om.readValue(obj.toString(), Root::class.java)

                        if (root.absoluteLayout != null) {
                            screenRatioFactor =
                                screenWidth / root.absoluteLayout!!.androidLayoutWidth!!.replace(
                                    "dp",
                                    ""
                                ).toDouble()

                            if (root.absoluteLayout!!.imageView != null) {
                                root.absoluteLayout!!.imageView!!.forEachIndexed { index, imageView ->
                                    imageViewArray.add(index, imageView)
                                }
                            }
                            if (root.absoluteLayout!!.textView != null) {
                                root.absoluteLayout?.textView?.forEachIndexed { index, textview ->
                                    textViewJson.add(index, textview)
                                }
                            }
                        }

                        if (imageViewArray.size > 0) {
                            addImage(imageViewArray)
                        }

                        if (textViewJson.size > 0) {
                            addText(textViewJson)
                        }

                        /* root.absoluteLayout?.androidLayoutWidth?.let {
                             screenRatioFactor = screenWidth / it.replace("dp", "").toDouble()
                         }

                         root.absoluteLayout?.imageView?.let {

                             if (it.size > 0) {

                                 it.forEachIndexed { index, element ->
                                     imageView?.add(index, element)
                                 }

                                 if (imageView != null && imageView!!.size > 0) {
                                     addImage(imageView!!)
                                     Log.d("myError", "Size is not  null")
                                 } else {
                                     Log.d("myTag", "image list is null")
                                 }

                             }else{
                                 Log.d("myError", "Size is null")
                             }
                         }

                         root.absoluteLayout?.textView?.let {
                             if (it.size > 0) {

                                 it.forEachIndexed { index, element ->
                                     textViewJson?.add(index, element)
                                 }

                                 if (textViewJson != null && textViewJson!!.size > 0) {
                                     addText(textViewJson!!)
                                 } else {
                                     Log.d("myTag", "Array size is null")
                                 }

                             }
                         }*/

                    } else {
                        Log.d("myError", "wrong json")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("myTag", "exception")
                }

            }
        })


    }

    var newImageView: android.widget.ImageView? = null

    private fun addImage(array: ArrayList<ImageView>) {

        array.forEachIndexed { _, imageView ->

            newImageView = android.widget.ImageView(this@MainActivity)

            if (imageView.appSrcCompat != null) {

                val resources: Resources = resources
                val resourceId: Int = resources.getIdentifier(
                    imageView.appSrcCompat.toString().replace("@drawable/", ""),
                    "drawable",
                    packageName
                )

                newImageView?.setImageDrawable(resources.getDrawable(resourceId))
            }

            if (imageView.androidBackground != null) {

                newImageView?.setBackgroundColor(Color.parseColor(imageView.androidBackground))
            }

            if (imageView.androidLayoutWidth != null && imageView.androidLayoutHeight != null) {

                val width = (imageView.androidLayoutWidth.toString()
                    .replace("dp", "")).toInt() * screenRatioFactor
                val height = (imageView.androidLayoutHeight.toString()
                    .replace("dp", "")).toInt() * screenRatioFactor
                val layoutParams = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
                newImageView?.layoutParams = layoutParams
            }

            if (imageView.androidLayoutX != null) {
                newImageView?.x = (imageView.androidLayoutX!!.replace("dp", "").toDouble()
                        * screenRatioFactor).toFloat()
            }

            if (imageView.androidLayoutY != null) {
                newImageView?.y = (imageView.androidLayoutY!!.replace("dp", "").toDouble()
                        * screenRatioFactor).toFloat()
            }

            if (imageView.androidRotation != null) {
                newImageView?.rotation = imageView.androidRotation!!.toFloat()
            }

            if (imageView.androidAlpha != null) {
                newImageView?.alpha = imageView.androidAlpha!!.toFloat()
            }

            rootLayout?.addView(newImageView)

        }

        /*it.forEachIndexed { index, imageView ->

            newImageView = android.widget.ImageView(this)

            it[index].appSrcCompat?.let {

                Log.d("myFile", "${it.toString()}")

                val resources: Resources = resources
                val resourceId: Int = resources.getIdentifier(
                    "${it.toString().replace("@drawable/", "")}",
                    "drawable",
                    packageName
                )

                newImageView?.setImageDrawable(resources.getDrawable(resourceId))
            }

            it[index].androidBackground?.let {
                newImageView?.setBackgroundColor(Color.parseColor(it))
            }

            val width = (it[index].androidLayoutWidth.toString()
                .replace("dp", "")).toInt() * screenRatioFactor
            val height = (it[index].androidLayoutHeight.toString()
                .replace("dp", "")).toInt() * screenRatioFactor

            val parms = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
            newImageView?.layoutParams = parms
            newImageView?.x =
                (it.get(index).androidLayoutX?.replace("dp", "")
                    ?.toFloat()?.times(screenRatioFactor))!!.toFloat()
            newImageView?.y =
                (it.get(index).androidLayoutY?.replace("dp", "")
                    ?.toFloat()?.times(screenRatioFactor))!!.toFloat()

            if (it[index].androidRotation != null) {
                Log.d("myRotation", "roration is  not null")
                newImageView?.rotation = it[index].androidRotation?.toFloat()!!
            } else {
                Log.d("myRotation", "roration is null")
            }

            it[index].androidAlpha?.let {
                newImageView?.alpha = it.toFloat()
            }

            rootLayout?.addView(newImageView)
        }*/

    }

    var newTextView: android.widget.TextView? = null

    private fun addText(arratlist: ArrayList<TextView>) {

        arratlist.forEachIndexed { _, textView ->

            newTextView = android.widget.TextView(this@MainActivity)

            if (!textView.androidLayoutWidth.equals("wrap_content") && !textView.androidLayoutHeight.equals(
                    "wrap_content"
                )
            ) {

                val width =
                    (textView.androidLayoutWidth.toString()
                        .replace("dp", "")).toInt() * screenRatioFactor
                val height =
                    (textView.androidLayoutHeight.toString()
                        .replace("dp", "")).toInt() * screenRatioFactor
                val parms = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
                newTextView?.layoutParams = parms

            }

            if (textView.androidText != null) {
                newTextView?.text = textView.androidText
            }

            if (textView.androidTextColor != null) {
                newTextView?.setTextColor(Color.parseColor(textView.androidTextColor))
            }

            if (textView.androidLayoutX != null) {
                newTextView?.x = (textView.androidLayoutX!!.replace("dp", "").toDouble()
                        * screenRatioFactor).toFloat()
            }

            if (textView.androidLayoutY != null) {
                newTextView?.y = (textView.androidLayoutY!!.replace("dp", "").toDouble()
                        * screenRatioFactor).toFloat()
            }

            if (textView.androidTextSize != null) {
                newTextView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    ((textView.androidTextSize?.replace(
                        "sp",
                        ""
                    ))?.toFloat()?.times(screenRatioFactor))!!.toFloat()
                )
            }

            if (textView.androidBackground != null) {
                newTextView?.setBackgroundColor(Color.parseColor(textView.androidBackground))
            }

            if (textView.androidLetterSpacing != null) {
                newTextView?.letterSpacing = textView.androidLetterSpacing!!.toFloat()
            }

            if (textView.androidRotation != null) {
                newTextView?.rotation = textView.androidRotation!!.toFloat()
            }

            if (textView.androidTextAlignment != null) {

                when {
                    textView.androidTextAlignment.equals("center", ignoreCase = true) -> {
                        newTextView?.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    textView.androidTextAlignment.equals("textEnd", ignoreCase = true) -> {
                        newTextView?.textAlignment = View.TEXT_ALIGNMENT_TEXT_END

                    }
                    textView.androidTextAlignment.equals("textStart", ignoreCase = true) -> {
                        newTextView?.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    }
                }
            }

            if (textView.androidAlpha != null) {
                newTextView?.alpha = textView.androidAlpha!!.toFloat()
            }

            if (textView.androidFontFamily != null) {

                val resources: Resources = resources
                val resourceId: Int = resources.getIdentifier(
                    textView.androidFontFamily.toString().replace("@font/", ""),
                    "font",
                    packageName
                )

                val typeface = ResourcesCompat.getFont(this@MainActivity, resourceId)
                if (typeface != null) {
                    newTextView?.typeface = typeface
                }

                if (textView.androidTextStyle != null) {
                    when {
                        textView.androidTextStyle.equals("bold") -> {
                            newTextView?.setTypeface(typeface, Typeface.BOLD)
                        }
                        textView.androidTextStyle.equals("italic") -> {
                            newTextView?.setTypeface(typeface, Typeface.ITALIC)
                        }
                        textView.androidTextStyle.equals("normal") -> {
                            newTextView?.setTypeface(typeface, Typeface.NORMAL)
                        }
                    }
                }

            }

            rootLayout?.addView(newTextView)
        }

    }

    private fun loadJSONFromAsset(): String? {

        var json: String? = null

        json = try {
            val `is`: InputStream = this.assets.open("2.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json

    }

    @Throws(IOException::class)
    fun AssetJSONFile(filename: String, context: Context): String? {
        val manager: AssetManager = context.assets
        val file = manager.open(filename)
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    }
}