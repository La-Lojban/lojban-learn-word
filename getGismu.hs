import XMLTools
import Data.Maybe

main = do
	cnt <- readFile "lojban_en.xml"
	let	Document _ _ topElem _ = xmlParse "" cnt
		elems = childrenE $ head $ childrenE topElem
		gismu = filter ((== Just "gismu") . flip getAttr "type") elems
		cmavo = filter ((== Just "cmavo") . flip getAttr "type") elems
		gandc = gismu ++ cmavo
	putStr $ makeXMLString gandc
--	putStr $ unlines $ map showElement gandc
--	putStr $ unlines $ map (fromJust . flip getAttr "word") gandc
--	putStr $ unlines $ map (fromJust . flip getAttr "word") gismu
--	putStr $ unlines $ map (fromJust . flip getAttr "word") cmavo
--	putStr $ unlines $ map (fromJust . flip getAttr "word") elems
