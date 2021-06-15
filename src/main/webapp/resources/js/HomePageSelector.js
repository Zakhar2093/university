function selectStudentOrTeachers(){
    var list = document.getElementById('id').getAttribute("list");
    console.log(list);
    if (list == "students") {
        document.querySelectorAll('.students').forEach(p => p.style.display = 'block');
        document.querySelectorAll('.teachers').forEach(p => p.style.display = 'none');
    }
    console.log(list == "teachers");
    if (list == "teachers"){
        document.querySelectorAll('.teachers').forEach(p => p.style.display = 'block');
        document.querySelectorAll('.students').forEach(p => p.style.display = 'none');
    }
}