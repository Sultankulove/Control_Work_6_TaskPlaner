<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Планировщик задач на ${currentDate}</title>
    <link rel="stylesheet" href="../style/calendar.css">
</head>
<body>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 20px;
    }
    h1 {
        text-align: center;
        color: #333;
    }
    .calendar {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
        max-width: 900px;
        margin: 0 auto;
    }
    .day-header {
        text-align: center;
        font-weight: bold;
        background-color: #ddd;
        padding: 10px;
    }
    .day {
        display: block;
        position: relative;
        text-align: left;
        padding: 10px;
        background-color: #fff;
        border: 1px solid #ddd;
        cursor: pointer;
        font-size: 14px;
        transition: background-color 0.3s ease;
        min-height: 80px;
        color: inherit;
        text-decoration: none;
    }
    .day:hover {
        background-color: #f0f0f0;
    }
    .current-day {
        background-color: #4CAF50;
        color: white;
    }
    .day-number {
        font-weight: bold;
        margin-bottom: 5px;
    }
    .tasks-container {
        margin-top: 5px;
    }
    .task-square {
        display: inline-block;
        width: 12px;
        height: 12px;
        margin: 2px;
        border-radius: 2px;
    }
    .task-square.срочная {
        background-color: red;
    }
    .task-square.работа {
        background-color: blue;
    }
    .task-square.покупки {
        background-color: green;
    }
    .task-square.обычная {
        background-color: orange;
    }
    .task-square.другое {
        background-color: gray;
    }
    .legend {
        max-width: 900px;
        margin: 20px auto 0;
        display: flex;
        gap: 15px;
        align-items: center;
        font-size: 14px;
    }
    .legend-item {
        display: flex;
        align-items: center;
        gap: 5px;
    }

</style>
<h1>Планировщик задач на ${currentDate}</h1>
<div class="calendar">
    <#list ["Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"] as dayOfWeek>
        <div class="day-header">${dayOfWeek}</div>
    </#list>
    <#list weeks as week>
        <#list week as day>
            <#if day.date?? && day.date != "">
                <a href="/tasks?date=${day.date}" class="day <#if day.currentDay>current-day</#if>">
                    <div class="day-number">${day.dayOfMonth}</div>
                    <div class="tasks-container">
                        <#list day.tasks as t>
                            <div class="task-square ${t.type}"></div>
                        </#list>
                    </div>
                </a>
            <#else>
                <div class="day"></div>
            </#if>
        </#list>
    </#list>
</div>
<div class="legend">
    <div class="legend-item">
        <div class="task-square срочная"></div>
        Срочная
    </div>
    <div class="legend-item">
        <div class="task-square работа"></div>
        Работа
    </div>
    <div class="legend-item">
        <div class="task-square покупки"></div>
        Покупки
    </div>
    <div class="legend-item">
        <div class="task-square обычная"></div>
        Обычная
    </div>
    <div class="legend-item">
        <div class="task-square другое"></div>
        Другое
    </div>
</div>
</body>
</html>
