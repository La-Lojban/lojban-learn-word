package iocikun.juj.lojban.learn.words

import _root_.java.io.{BufferedReader, InputStreamReader}
import _root_.scala.util.Random
import _root_.scala.xml.{XML, Node}

import _root_.android.app.Activity
import _root_.android.os.Bundle

import _root_.android.text.Html
import _root_.android.view.View
import _root_.android.view.View.OnClickListener

class LojbanLearnWords extends Activity with TypedActivity {

	lazy val next = findView(TR.next)
	lazy val asset = getAssets()
	lazy val file = new BufferedReader(new InputStreamReader(
		asset.open("valsi_en.xml"), "UTF-8"))
	lazy val xml = XML.load(file)
	lazy val valsi = xml \ "valsi"
	lazy val n = valsi.length
	lazy val valsiview = findView(TR.valsi)
	lazy val textview = findView(TR.textview)

	override def onCreate(bundle: Bundle) {
		super.onCreate(bundle)
		setContentView(R.layout.main)

		showValsi

		next setOnClickListener new OnClickListener() {
			def onClick(v: View) = showValsi
		}
	}

	def showValsi = {
		val rand = new Random()
		val line = makeValsiString(valsi(rand nextInt n))
		valsiview.setTextSize(30)
		valsiview.setText(Html.fromHtml(line._1))
		textview.setText(Html.fromHtml(line._2))
	}



	var mr = new MyRegex

	def makeValsiString(valsi: Node): (String, String) = {
		var rafsiStr = ""
		for (r <- valsi \ "rafsi") rafsiStr += "<BR/><B>rafsi</B>: " + r.text
		return ((valsi \ "@word").text, "<B>type</B>: " +
			valsi \ "@type" + rafsiStr +
			"<BR/><B>definition</B>: " +
			mr.rep((valsi \ "definition").text) +
			"<BR/><B>notes</B>: " +
			mr.rep((valsi \ "notes").text.filterNot
				{c => '{'.equals(c) || '}'.equals(c)}))
	}
}
