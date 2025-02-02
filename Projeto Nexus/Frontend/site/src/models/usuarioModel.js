var database = require("../database/config")

function autenticar(email, token) {
    var instrucao = `
    SELECT * FROM Funcionario 
    JOIN Empresa ON Funcionario.fkEmpresa = Empresa.idEmpresa 
    JOIN Maquina on Funcionario.idFuncionario = Maquina.fkFuncionario
    WHERE Funcionario.emailCorporativo = '${email}' AND Funcionario.token = '${token}';
    `;
    console.log("Executando a instrução SQL: \n" + instrucao);
    return database.executar(instrucao);
}

// Coloque os mesmos parâmetros aqui. Vá para a var instrucao
function cadastrarFuncionario(firstName, lastName, email, phoneDDD, phoneNumber, role, agencyEmpre) {
    console.log("model")
    var instrucao = `
        INSERT INTO Funcionario (nome, sobrenome, emailCorporativo, ddd, telefone, cargo, situacao, fkAgencia, fkEmpresa) VALUES ('${firstName}', '${lastName}', '${email}', '${phoneDDD}', '${phoneNumber}', '${role}', 'Ativo', 1, (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${agencyEmpre}'));
    `;
    console.log("Executando a instrução SQL: \n" + instrucao);
    return database.executar(instrucao);
}

function cadastrarEndAgencia(cep, endereco, bairro, cidade, uf, complemento) {
    var instrucao = `
    INSERT INTO Endereco (cep, logradouro, bairro, localidade, uf, complemento)
    VALUES ('${cep}', '${endereco}', '${bairro}', '${cidade}', '${uf}', '${complemento}');
    `;
    console.log("Executando a instrução SQL: \n" + instrucao);
    return database.executar(instrucao);
}

function cadastrarAgencia(agencia, digito, organizacao, email, ddd, telefone, endereco, emailFunc) {
    var instrucao = `
    INSERT INTO Agencia (numero, digitoAgencia, ddd, telefone, email, fkEmpresa, fkEndereco)
    VALUES
    ('${agencia}', '${digito}', '${ddd}', '${telefone}', '${email}', (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${organizacao}'), (SELECT idEndereco FROM Endereco WHERE logradouro = '${endereco}'));
    UPDATE Funcionario
    SET fkAgencia = (SELECT idAgencia FROM Agencia WHERE numero = '${agencia}')
    WHERE idFuncionario = (SELECT idFuncionario FROM Funcionario WHERE emailCorporativo = '${emailFunc}');
    `;


    console.log("Executando a instrução SQL: \n" + instrucao);
    return database.executar(instrucao);
}

function listarFuncionario(agencia, empresa) {
    console.log(agencia);

    var instrucao = `
        SELECT Funcionario.idFuncionario, Funcionario.nome
        FROM Funcionario
        INNER JOIN Agencia ON Funcionario.fkAgencia = Agencia.idAgencia
        INNER JOIN Empresa ON Agencia.fkEmpresa = Empresa.idEmpresa
        WHERE Agencia.numero = '${agencia}'
        AND Empresa.nomeEmpresa = '${empresa}';
    `;

    return database.executar(instrucao);
}

function listarEmpresa() {

    var instrucao = `
    SELECT nomeEmpresa FROM Empresa;
    `;
    return database.executar(instrucao);
}

function cadastrarMaquina(brand, model, situation, funcionario, agencia, banco, email) {

    var instrucao = `
    INSERT INTO Maquina (marca, modelo, situacao, fkFuncionario, fkAgencia, fkEmpresa) VALUES ('${brand}', '${model}', '${situation}', (SELECT idFuncionario FROM Funcionario WHERE emailCorporativo = '${email}'), (SELECT idAgencia FROM Agencia WHERE numero = ${agencia}), (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}'));
    `;

    return database.executar(instrucao);
}

function cadastrarToken(email, token) {

    var instrucao = `
    UPDATE Funcionario
        SET token = '${token}'
        WHERE emailCorporativo = '${email}';`;

    return database.executar(instrucao);
}

module.exports = {
    autenticar,
    cadastrarFuncionario,
    cadastrarEndAgencia,
    cadastrarAgencia,
    listarFuncionario,
    cadastrarMaquina,
    cadastrarToken,
    listarEmpresa
};