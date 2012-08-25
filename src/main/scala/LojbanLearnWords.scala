package iocikun.juj.lojban.learn.words

import _root_.java.io.{BufferedReader, InputStreamReader}
import _root_.scala.util.Random
import _root_.scala.xml.{XML, Node, NodeSeq}

import _root_.android.app.Activity
import _root_.android.os.Bundle

import _root_.android.text.Html
import _root_.android.view.View
import _root_.android.view.View.OnClickListener

import _root_.android.content.Context
import _root_.android.content.SharedPreferences

import _root_.android.util.Log

class LojbanLearnWords extends Activity with TypedActivity {

	lazy val next = findView(TR.next)
	lazy val know = findView(TR.i_know)
	lazy val asset = getAssets()
	lazy val file = new BufferedReader(new InputStreamReader(
		asset.open("valsi_en.xml"), "UTF-8"))
	lazy val xml = XML.load(file)
	lazy val valsiview = findView(TR.valsi)
	lazy val textview = findView(TR.textview)

	lazy val sharedpreferences =
		getSharedPreferences("knows", Context.MODE_PRIVATE)
	lazy val editor =
		sharedpreferences.edit()

	var valsi: NodeSeq = List()
	var nowValsi = ""
	var n = 0

	var minimal = 0

	lazy val scoreview = findView(TR.score)
	var score = 0

	override def onCreate(bundle: Bundle) {
		super.onCreate(bundle)
		setContentView(R.layout.main)

		valsi = xml \ "valsi"
		minimal = sharedpreferences.getInt("minimal", 0)
		valsi = valsi filter { v =>
			val w = (v \ "@word")(0).text
			val p = sharedpreferences.getInt(w, 0)
			score += p
			p == minimal
		}

		n = valsi.length
		Log.d("LojbanLearnWords", "valsi not learned is " + n)

		showValsi
		scoreview.setText("" + score)

		know setOnClickListener new OnClickListener() {
			def onClick(v: View) {
				val point = sharedpreferences.getInt(nowValsi, 0)
				valsiview.setText(nowValsi + " " + point)
				editor.putInt(nowValsi, point + 1)
				valsi = valsi filter (v =>
					(v \ "@word")(0).text != nowValsi)
				n -= 1
				if (n == 0) {
					valsi = xml \ "valsi"
					n = valsi.length
					minimal += 1
					editor.putInt("minimal", minimal)
				}
				showValsi
				score += 1
				scoreview.setText("" + score)
				editor.commit
				Log.d("LojbanLearnWords", "valsi not learned is " + n)
				val nd = valsi.length
				Log.d("LojbanLearnWords",
					"valsi not learned is " + nd + " (real)")

			}
		}

		next setOnClickListener new OnClickListener() {
			def onClick(v: View) = showValsi
		}
	}

	def showValsi = {
		val rand = new Random()
		val line = makeValsiString(valsi(rand nextInt n))
		nowValsi = line._1

		valsiview.setTextSize(30)
		valsiview.setText(nowValsi)
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
