package com.juagri.testimagemap

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.juagri.testimagemap.InternalLinkMovementMethod.OnLinkClickedListener
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity4 : AppCompatActivity() {
    private lateinit var root:View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)
        root = findViewById(R.id.root)
        var htmlContent = "<a href=\"https://google.com\">Click here to open</a> <b>browser</b>....<a href=\"tel:2125551212\">Click here to call</a> and find <i>the below number</i> to identify the phone no [tel]0123456789[/tel] and fine the second no [tel]9876543210[/tel]"
        val textView = findViewById<TextView>(R.id.html_text_view)
        val logView = findViewById<TextView>(R.id.log_view)
        getContentString(htmlContent,logView).forEach {
            htmlContent = htmlContent.replace(it.from,it.to)
        }
        textView.text = Html.fromHtml(htmlContent,Html.FROM_HTML_MODE_LEGACY)
        textView.movementMethod = InternalLinkMovementMethod(linkClickListener)
    }

    private fun getContentString(oldText:String,logView:TextView):List<ReplaceableString>{
        val replaceableString = mutableListOf<ReplaceableString>()
        val pattern: Pattern = Pattern.compile("\\[tel](.*?)\\[/tel]")
        val matcher: Matcher = pattern.matcher(oldText)
        while (matcher.find()) {
            val phoneNo = matcher.group(1)
            replaceableString.add(ReplaceableString(matcher.group(0),"<a href=\"tel:$phoneNo\">$phoneNo</a>"))
            logView.text = logView.text.toString() +"\n" + phoneNo
        }
        return replaceableString
    }

    private val linkClickListener = object : OnLinkClickedListener {
        override fun onLinkClicked(url: String?): Boolean {
            val link = url.value()
            if (link.startsWith("http")) {
                Snackbar.make(root, "Clickable Url: $link", Snackbar.LENGTH_LONG).show()
            } else if (link.startsWith("tel")) {
                Snackbar.make(root, "Clickable Tel No: $link", Snackbar.LENGTH_LONG).show()
            } else if (link.startsWith("email")) {
                Snackbar.make(root, "Clickable EMail: $link", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(root, "Clickable Unknown: $link", Snackbar.LENGTH_LONG).show()
            }
            return true
        }
    }
    data class ReplaceableString(val from: String,val to:String)
}

private fun getUrls(logView:TextView){
        val textView = findViewById<TextView>(R.id.html_text_view2)
        var htmlContent = "https://google.com Click here to open https://bbc.com"
        val pattern = """(?i)<a\s+[^>]*>[^<]*</a>|(https?|ftp)://(?:-\.)?([^\s/?.#-]+\.?)+(/\S*)?"""
        val urlpattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = urlpattern.matcher(htmlContent)
        val replaceableString = mutableListOf<ReplaceableString>()
        while (matcher.find()) {
            val url = matcher.group(0)
            replaceableString.add(ReplaceableString(matcher.group(0),"<a href=\"$url\">$url</a>"))
            logView.text = logView.text.toString() +"\n" + url
        }
        replaceableString.forEach {
            htmlContent = htmlContent.replace(it.from,it.to)
        }
        textView.text = Html.fromHtml(htmlContent,Html.FROM_HTML_MODE_LEGACY)
    }

fun String?.value():String{
    return this ?: ""
}
