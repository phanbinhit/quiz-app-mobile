const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const ExamSchema = new Schema({
    roomId: {type: String, required: true},
    questions: {type: Array, default: [], required: true},
    time: {type: Number, required: true},
    results: {type: Array, default: [], require: true} 
});

module.exports = mongoose.model('Exam', ExamSchema, 'exams');