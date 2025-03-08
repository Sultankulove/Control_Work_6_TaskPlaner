<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Новая задача на ${date}</title>
    <link rel="stylesheet" type="text/css" href="../style/taskAdd.css">
</head>
<body>
<style>
    body {
        font-family: sans-serif;
        margin: 20px;
    }
    h1 {
        margin: 0 0 15px 0;
    }
    form {
        display: flex;
        gap: 10px;
        align-items: center;
    }
    input[type="text"] {
        width: 220px;
    }

</style>
<h1>Новая задача на ${date}</h1>
<form method="POST" action="/task/add">
    <input type="hidden" name="date" value="${date}">
    <input type="text" name="name" required>
    <input type="text" name="description">
    <select name="type">
        <option value="обычная">обычная</option>
        <option value="срочная">срочная</option>
        <option value="работа">работа</option>
        <option value="покупки">покупки</option>
        <option value="другое">другое</option>
    </select>
    <button type="submit">Добавить</button>
</form>
</body>
</html>
