package vycoder

import scala.collection.mutable.ListBuffer
object Codepage:
  var codepage = "λƛ¬∧⟑∨⟇÷×«\n»°•ß†€"
  codepage += "½∆ø↔¢⌐æʀʁɾɽÞƈ∞¨ "
  codepage += "!\"#$%&'()*+,-./01"
  codepage += "23456789:;<=>?@A"
  codepage += "BCDEFGHIJKLMNOPQ"
  codepage += "RSTUVWXYZ[\\]`^_abc"
  codepage += "defghijklmnopqrs"
  codepage += "tuvwxyz{|}~↑↓∴∵›"
  codepage += "‹∷¤ð→←βτȧḃċḋėḟġḣ"
  codepage += "ḭŀṁṅȯṗṙṡṫẇẋẏż√⟨⟩"
  codepage += "‛₀₁₂₃₄₅₆₇₈¶⁋§ε¡"
  codepage += "∑¦≈µȦḂĊḊĖḞĠḢİĿṀṄ"
  codepage += "ȮṖṘṠṪẆẊẎŻ₌₍⁰¹²∇⌈"
  codepage += "⌊¯±₴…□↳↲⋏⋎꘍ꜝ℅≤≥"
  codepage += "≠⁼ƒɖ∪∩⊍£¥⇧⇩ǍǎǏǐǑ"
  codepage += "ǒǓǔ⁽‡≬⁺↵⅛¼¾Π„‟"

  def vyxalToInt(s: String): Seq[Int] =
    s.map(codepage.indexOf(_))

  def intToVyxal(lst: Seq[Int]): String =
    lst.map(codepage(_)).mkString("")
