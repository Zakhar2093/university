const SPACE = " ";
const COLON = ":";

var x = document.querySelectorAll(".date");
var i;
for (i = 0; i < x.length ;i++) {
     var date = x[i].textContent;
     console.log(date);
     var year = date.substr(0, 4);
     var month = date.substr(5, 2);
     var day = date.substr(8, 2);
     var hour = date.substr(11, 2);
     var minute = date.substr(14, 2);
     var AMorPM = "AM";

     if (hour == 0) {
          hour = 12;
          AMorPm = "AM";
     } else if (hour > 12) {
          hour -= 12;
          AMorPM = "PM";
     }
     var result = day + SPACE + month + SPACE + year + SPACE + hour + COLON + minute + SPACE + AMorPM;
     x[i].innerHTML = result;
}

