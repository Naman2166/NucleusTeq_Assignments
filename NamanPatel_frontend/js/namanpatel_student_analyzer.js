// I created an array of students with their data
const students = [
  {
    name: "Lalit",
    marks: [
      { subject: "Math", score: 78 },
      { subject: "English", score: 82 },
      { subject: "Science", score: 74 },
      { subject: "History", score: 69 },
      { subject: "Computer", score: 88 }
    ],
    attendance: 82
  },
  {
    name: "Rahul",
    marks: [
      { subject: "Math", score: 90 },
      { subject: "English", score: 85 },
      { subject: "Science", score: 80 },
      { subject: "History", score: 76 },
      { subject: "Computer", score: 92 }
    ],
    attendance: 91
  },
  {
    name: "Naman",
    marks: [
      { subject: "Math", score: 92 },
      { subject: "English", score: 86 },
      { subject: "Science", score: 75 },
      { subject: "History", score: 72 },
      { subject: "Computer", score: 95 }
    ],
    attendance: 86
  },

];



// it is a function to calculate total marks of a student
function totalMarks(student) {
  let total = 0;

  for (let i = 0; i < student.marks.length; i++) {
    total = total + student.marks[i].score;
  }
  return total;
}

//printing total marks of each student
for (let i=0; i < students.length; i++) {
  let total = totalMarks(students[i]);
  console.log(students[i].name + " total marks = " + total);
}




// it is function to calculate average marks of a student
function averageMarks(student) {
  let total = totalMarks(student);
  let avg = total/student.marks.length;
  return avg;
}

//printing average marks of each student
for (let i=0; i<students.length; i++) {
  let avg = averageMarks(students[i]);
  console.log(students[i].name + " average marks = " + avg);
}




//it calculates highest marks scored in each subject
let subjects = ["Math", "English", "Science", "History", "Computer"];

//loop through each subject
for (let i= 0; i < subjects.length; i++) {
  let maxMarks = 0;
  let topper = "";
  
  for (let j = 0; j < students.length; j++) {                 
    for (let k = 0; k < students[j].marks.length; k++) {  
      let currentSub = students[j].marks[k];      

      //here if subject matches we will comapare marks with max marks
      if (currentSub.subject === subjects[i]) {              

        if (currentSub.score > maxMarks) {
          maxMarks = currentSub.score;
          topper = students[j].name;
        }
      }
    }
  }
  
  console.log("highest in " + subjects[i] + " : " + topper +"("+ maxMarks +")" )
}


