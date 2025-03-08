<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактировать задачу</title>
    <link rel="stylesheet" type="text/css" href="../style/taskEdit.css">
</head>
<body>
<style>
    body {
        font-family: sans-serif;
        margin: 20px;
    }
    h1 {
        margin-bottom: 15px;
    }
    form {
        display: flex;
        flex-direction: column;
        gap: 10px;
        max-width: 400px;
    }
    input[type="text"],
    textarea,
    select {
        padding: 5px;
        font-size: 14px;
        width: 100%;
    }
    button {
        padding: 8px 12px;
        font-size: 14px;
        cursor: pointer;
    }

</style>
<h1>Редактировать задачу: ${task.name}</h1>
<form method="POST" action="/task/edit">
    <input type="hidden" name="oldName" value="${task.name}">
    <input type="hidden" name="date" value="${dateIso}">
    <label>Новое название задачи:</label>
    <input type="text" name="newName" value="${task.name}" required>
    <label>Описание задачи:</label>
    <textarea name="description">${task.description}</textarea>
    <label>Категория:</label>
    <select name="type">
        <option value="обычная" <#if task.type?lower_case == "обычная">selected</#if>>Обычная</option>
        <option value="срочная" <#if task.type?lower_case == "срочная">selected</#if>>Срочная</option>
        <option value="работа" <#if task.type?lower_case == "работа">selected</#if>>Работа</option>
        <option value="покупки" <#if task.type?lower_case == "покупки">selected</#if>>Покупки</option>
        <option value="другое" <#if task.type?lower_case == "другое">selected</#if>>Другое</option>
    </select>
    <button type="submit">Сохранить изменения</button>
</form>
</body>
</html>
