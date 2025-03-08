<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Задачи на ${dateDisplay}</title>
    <link rel="stylesheet" type="text/css" href="../style/taskList.css">
</head>
<body>
<style>
    body {
        font-family: sans-serif;
        margin: 0;
        padding: 20px;
    }
    .back-link {
        color: #000;
        text-decoration: none;
        margin-bottom: 10px;
        display: inline-block;
    }
    .back-link:hover {
        text-decoration: underline;
    }
    h1 {
        margin-top: 0;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 10px;
    }
    th {
        background-color: #eee;
        text-align: left;
        padding: 8px;
        border: 1px solid #ccc;
    }
    td {
        border: 1px solid #ccc;
        padding: 8px;
        vertical-align: top;
    }
    tr.task-row {
        background-color: #f8f8f8;
    }
    .add-task-link {
        text-decoration: none;
        color: #000;
    }
    .add-task-link:hover {
        text-decoration: underline;
    }
    a.delete-link,
    a.edit-link {
        color: #000;
        text-decoration: none;
        margin-right: 10px;
    }
    a.delete-link:hover,
    a.edit-link:hover {
        text-decoration: underline;
    }
    .type-square {
        display: inline-block;
        width: 12px;
        height: 12px;
        margin-right: 5px;
        border-radius: 2px;
        vertical-align: middle;
    }
    .type-square.срочная {
        background-color: red;
    }
    .type-square.работа {
        background-color: blue;
    }
    .type-square.покупки {
        background-color: green;
    }
    .type-square.обычная {
        background-color: orange;
    }
    .type-square.другое {
        background-color: gray;
    }

</style>
<a href="/calendar" class="back-link">&#8592; Назад к календарю</a>
<h1>Задачи на ${dateDisplay}</h1>
<table>
    <thead>
    <tr>
        <th>Тип</th>
        <th>Название</th>
        <th>Описание</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <#if tasks?? && (tasks?size > 0)>
        <#list tasks as task>
            <tr <#if (task_index % 2) == 0>class="task-row"</#if>>
                <td>
                    <span class="type-square ${task.type?lower_case}"></span>
                    ${task.type}
                </td>
                <td>${task.name}</td>
                <td>${task.description}</td>
                <td>
                    <form action="/task/delete" method="POST" style="display:inline;">
                        <input type="hidden" name="taskName" value="${task.name}">
                        <input type="hidden" name="date" value="${dateIso}">
                        <button type="submit" class="delete-link" style="background:none; border:none; padding:0; cursor:pointer;">Удалить</button>
                    </form>
                    <a href="/task/edit?taskName=${task.name}&date=${dateIso}" class="edit-link">Редактировать</a>
                </td>
            </tr>
        </#list>
    <#else>
        <tr>
            <td colspan="4" style="text-align:center; font-style: italic;">Задачи на эту дату отсутствуют.</td>
        </tr>
    </#if>
    </tbody>
</table>
<a href="/task/add?date=${dateIso}" class="add-task-link">Добавить задачу</a>
</body>
</html>
