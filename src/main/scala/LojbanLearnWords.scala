package iocikun.juj.lojban.learn.words

import _root_.java.io.{BufferedReader, InputStreamReader}
import _root_.scala.util.Random
import _root_.scala.xml.{XML, Node}

import _root_.android.app.Activity
import _root_.android.os.Bundle

import _root_.android.text.Html

class LojbanLearnWords extends Activity with TypedActivity {
	override def onCreate(bundle: Bundle) {
		super.onCreate(bundle)
		setContentView(R.layout.main)

		val asset = getAssets()
		val file = new BufferedReader(new InputStreamReader(
			asset.open("valsi_en.xml"), "UTF-8"))
		val xml = XML.load(file)
		val valsi = xml \ "valsi"
		val rand = new Random()
		val n = valsi.length
		val line = makeValsiString(valsi(rand nextInt n))

		val valsiView = findView(TR.valsi)
		valsiView.setTextSize(30)
		valsiView.setText(Html.fromHtml(line._1))
		findView(TR.textview).setText(Html.fromHtml(line._2))
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
