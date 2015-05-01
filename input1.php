
$a=3;
$a=$a+3;
$a=$a-3;
$a=$a*3;
$a=$a/3;
$a=$a%3;
$b=$a;
$c=$a+$b;
echo $a;
print $a;
echo "Text1";
for($a = 0; $a < 5; $a++) {
echo $a;
}
while($a < 5) {
echo $a;
$a++;
}
if ($a == 0) {
echo "Text1";
}
else if ($a == 1) {
echo "Text2";
}
else {
echo "Text3";
}
echo "String 1\n";
$string_var = "String 2";
echo $string_var."\n";
echo "String 1"." ".$string_var;
f(2);
function f($a) {
echo $a;
}