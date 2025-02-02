var database = require("../database/config");

function listarAgenciasNOC(empresa) {

  var instrucao = `
  SELECT
  Agencia.numero AS CodigoAgencia,
  Agencia.digitoAgencia AS DigitoAgencia,
  COUNT(DISTINCT Maquina.idMaquina) AS TotalMaquinas,
  ROUND(SUM(CASE WHEN Alerta.causa = 'Sobrecarga de CPU' AND Alerta.gravidade = 'Alta'
      THEN Registro.usoAtual ELSE 0 END), 2) AS TotalUsoCPU,
  ROUND(SUM(CASE WHEN Alerta.causa = 'Erro de disco rígido' AND Alerta.gravidade = 'Alta'
      THEN Registro.usoAtual ELSE 0 END), 2) AS TotalUsoDisco,
  ROUND(SUM(CASE WHEN Alerta.causa = 'Memória insuficiente' AND Alerta.gravidade = 'Alta'
      THEN Registro.usoAtual ELSE 0 END), 2) AS TotalUsoRAM,
  ROUND(SUM(Registro.capacidadeMax), 2) AS TotalCapacidade,
  SUM(CASE WHEN (Registro.usoAtual / Registro.capacidadeMax) * 100 > 70 THEN 1 ELSE 0 END) AS MaquinasAcimaDe70Percent
FROM Agencia
JOIN Maquina ON Agencia.idAgencia = Maquina.fkAgencia
LEFT JOIN Registro ON Maquina.idMaquina = Registro.fkMaquina
LEFT JOIN Alerta ON Registro.fkAlerta = Alerta.idAlerta
WHERE Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${empresa}')
GROUP BY Agencia.idAgencia, Agencia.numero, Agencia.digitoAgencia;
`;

  return database.executar(instrucao);

}

function listarLocalizacao(empresa) {
  var instrucao = `
  SELECT
  idMaquina,
  Registro.enderecoIPV4 AS "endereco",
  Agencia.digitoAgencia
  FROM Registro
  JOIN Maquina ON Maquina.idMaquina = Registro.fkMaquina
  JOIN Agencia ON Agencia.idAgencia = Maquina.fkAgencia
WHERE Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${empresa}')
GROUP BY Maquina.idMaquina, Registro.enderecoIPV4, Agencia.digitoAgencia;
  `;

  return database.executar(instrucao);

}

function listarProcessos(idMaq) {
  var instrucao = `
  SELECT
  Processo.idProcesso,
  Processo.fkMaquina,
  Processo.nome AS NomeProcesso,
  FORMAT(Processo.dataHora, 'HH:mm:ss') AS DataHora,
  Processo.usoAtualRAM AS UsoRam,
  Processo.usoAtualDisco AS UsoDisco,
  Processo.usoAtualCPU AS UsoCPU
FROM 
  Processo
WHERE 
  Processo.fkMaquina = ${idMaq}
ORDER BY 
  Processo.usoAtualCPU DESC
OFFSET 0 ROWS
FETCH NEXT 5 ROWS ONLY;`

  return database.executar(instrucao);
}

function statusMaquinas(agencia, banco, idMaq) {
  var instrucao = `
  SELECT DISTINCT
    Componente.nome AS NomeComponente,
    Registro.capacidadeMax AS CapacidadeMaxima,
    Registro.usoAtual AS UsoAtual,
    Maquina.situacao AS Status,
    Maquina.idMaquina AS IdMaquina,
    FORMAT(Registro.dataHora, 'HH:mm:ss') AS DataHora,
    Registro.enderecoIPV4 AS EnderecoIP
FROM (
    SELECT
        fkComponente,
        MAX(Registro.dataHora) AS max_dataHora
    FROM Registro
    GROUP BY fkComponente
) AS ultimos_registros
INNER JOIN Componente ON ultimos_registros.fkComponente = Componente.idComponente
INNER JOIN Registro ON ultimos_registros.fkComponente = Registro.fkComponente AND ultimos_registros.max_dataHora = Registro.dataHora
INNER JOIN Maquina ON Registro.fkMaquina = Maquina.idMaquina
INNER JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
INNER JOIN Alerta ON Registro.fkAlerta = Alerta.idAlerta
WHERE 
    Maquina.idMaquina = ${idMaq}
    AND Maquina.fkAgencia = ${agencia}
    AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}');
  `

  return database.executar(instrucao);
}

function listarMaquinas(banco) {
  var instrucao = `
  WITH RankedRecords AS (
    SELECT
      Componente.nome AS NomeComponente,
      Registro.capacidadeMax AS CapacidadeMaxima,
      Registro.usoAtual AS UsoAtual,
      Maquina.situacao AS Status,
      Maquina.idMaquina AS IdMaquina,
      Agencia.numero AS NumeroAgencia,
      FORMAT(Registro.dataHora, 'HH:mm:ss') AS DataHora,
      Registro.enderecoIPV4 AS EnderecoIP,
      ((Registro.usoAtual / Registro.capacidadeMax) * 100) AS Porcentagem,
      ROW_NUMBER() OVER (PARTITION BY Componente.nome ORDER BY (Registro.usoAtual / Registro.capacidadeMax) DESC) AS RowNum
    FROM
      Maquina
      JOIN Registro ON Registro.fkMaquina = Maquina.idMaquina
      JOIN Componente ON Registro.fkComponente = Componente.idComponente
      JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
      JOIN Empresa ON Agencia.fkEmpresa = Empresa.idEmpresa
    WHERE
      Empresa.idEmpresa = (SELECT idEmpresa FROM Empresa WHERE Empresa.nomeEmpresa = '${banco}')
  )
  SELECT
    NomeComponente,
    MAX(CapacidadeMaxima) AS CapacidadeMaxima,
    MAX(UsoAtual) AS UsoAtual,
    MAX(Status) AS Status,
    MAX(IdMaquina) AS IdMaquina,
    MAX(NumeroAgencia) AS NumeroAgencia,
    MAX(DataHora) AS DataHora,
    MAX(EnderecoIP) AS EnderecoIP,
    MAX(Porcentagem) AS MaiorPorcentagem
  FROM
    RankedRecords
  WHERE
    RowNum = 1
  GROUP BY
    NomeComponente;
  
`;

  return database.executar(instrucao);
}

function listarMaquinasAg(agencia) {
  var instrucao = `
  SELECT * FROM Maquina JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia  WHERE Agencia.idAgencia = '${agencia}';
  `;

  return database.executar(instrucao)
}

function altoConsumoCPU(banco, agencia) {
  var instrucao = `
  WITH rankedRows AS (
    SELECT
        Agencia.numero AS CodigoAgencia,
        Maquina.idMaquina AS NumeroMaquina,
        Registro.capacidadeMax AS TotalCapacidade,
        Registro.usoAtual AS ConsumoAtual,
        (Registro.usoAtual / Registro.capacidadeMax) * 100 AS Porcentagem,
        ROW_NUMBER() OVER (ORDER BY (Registro.usoAtual - Registro.capacidadeMax) DESC) AS RowNum
    FROM 
        Registro
    JOIN 
        Maquina ON Registro.fkMaquina = Maquina.idMaquina
    JOIN 
        Agencia ON Maquina.fkAgencia = Agencia.idAgencia
    WHERE 
        Registro.fkAlerta = (SELECT idAlerta FROM Alerta WHERE causa = 'Sobrecarga de CPU' AND gravidade = 'Alta')
        AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
        AND Agencia.idAgencia = (SELECT idAgencia FROM Agencia WHERE numero = '${agencia}')
)
SELECT 
    CodigoAgencia,
    NumeroMaquina,
    TotalCapacidade,
    ConsumoAtual,
    Porcentagem,
    (SELECT AVG(Porcentagem) FROM rankedRows) AS MediaPorcentagem
FROM rankedRows
WHERE RowNum = 1;
  `;

  return database.executar(instrucao);
}

function altoConsumoRAM(banco, agencia) {
  var instrucao = `
WITH rankedRows AS (
  SELECT
      Agencia.numero AS CodigoAgencia,
      Maquina.idMaquina AS NumeroMaquina,
      Registro.capacidadeMax AS TotalCapacidade,
      Registro.usoAtual AS ConsumoAtual,
      (Registro.usoAtual / Registro.capacidadeMax) * 100 AS Porcentagem,
      ROW_NUMBER() OVER (ORDER BY (Registro.usoAtual - Registro.capacidadeMax) DESC) AS RowNum
  FROM 
      Registro
  JOIN 
      Maquina ON Registro.fkMaquina = Maquina.idMaquina
  JOIN 
      Agencia ON Maquina.fkAgencia = Agencia.idAgencia
  WHERE 
      Registro.fkAlerta = (SELECT idAlerta FROM Alerta WHERE causa = 'Memória insuficiente' AND gravidade = 'Alta')
      AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
      AND Agencia.idAgencia = (SELECT idAgencia FROM Agencia WHERE numero = '${agencia}')
)
SELECT 
  CodigoAgencia,
  NumeroMaquina,
  TotalCapacidade,
  ConsumoAtual,
  Porcentagem,
  (SELECT AVG(Porcentagem) FROM rankedRows) AS MediaPorcentagem
FROM rankedRows
WHERE RowNum = 1;`;

  return database.executar(instrucao);
}

function altoConsumoDisco(banco, agencia) {
  var instrucao = `
  WITH rankedRows AS (
    SELECT
        Agencia.numero AS CodigoAgencia,
        Maquina.idMaquina AS NumeroMaquina,
        Registro.capacidadeMax AS TotalCapacidade,
        Registro.usoAtual AS ConsumoAtual,
        (Registro.usoAtual / Registro.capacidadeMax) * 100 AS Porcentagem,
        ROW_NUMBER() OVER (ORDER BY (Registro.usoAtual - Registro.capacidadeMax) DESC) AS RowNum
    FROM 
        Registro
    JOIN 
        Maquina ON Registro.fkMaquina = Maquina.idMaquina
    JOIN 
        Agencia ON Maquina.fkAgencia = Agencia.idAgencia
    WHERE 
        Registro.fkAlerta = (SELECT idAlerta FROM Alerta WHERE causa = 'Erro de disco rígido' AND gravidade = 'Alta')
        AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
        AND Agencia.idAgencia = (SELECT idAgencia FROM Agencia WHERE numero = '${agencia}')
  )
  SELECT 
    CodigoAgencia,
    NumeroMaquina,
    TotalCapacidade,
    ConsumoAtual,
    Porcentagem,
    (SELECT AVG(Porcentagem) FROM rankedRows) AS MediaPorcentagem
  FROM rankedRows
  WHERE RowNum = 1;
  `;

  return database.executar(instrucao);
}

function altoConsumoCPUFunc(banco, agencia, funcionario) {
  var instrucao = `
  SELECT TOP 1 Registro.capacidadeMax AS TotalCapacidade,
  Registro.usoAtual AS ConsumoAtual,
  FORMAT(Registro.dataHora, 'HH:mm:ss') AS DataHora
FROM Registro 
JOIN Alerta ON Registro.fkAlerta = Alerta.idAlerta
JOIN Maquina ON Registro.fkMaquina = Maquina.idMaquina
JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
WHERE causa = 'Sobrecarga de CPU'
AND gravidade IN ('Alta', 'Media', 'Baixa')
AND Maquina.fkFuncionario = ${funcionario}
AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
ORDER BY (Registro.usoAtual - Registro.capacidadeMax) DESC;
  `;

  return database.executar(instrucao);
}

function altoConsumoRAMFunc(banco, agencia, funcionario) {
  var instrucao = `
  SELECT TOP 1 Registro.capacidadeMax AS TotalCapacidade,
  Registro.usoAtual AS ConsumoAtual,
  FORMAT(Registro.dataHora, 'HH:mm:ss') AS DataHora
FROM Registro 
JOIN Alerta ON Registro.fkAlerta = Alerta.idAlerta
JOIN Maquina ON Registro.fkMaquina = Maquina.idMaquina
JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
WHERE causa = 'Memória insuficiente'
AND gravidade IN ('Alta', 'Media', 'Baixa')
AND Maquina.fkFuncionario = ${funcionario}
AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
ORDER BY (Registro.usoAtual - Registro.capacidadeMax) DESC;
  `;

  return database.executar(instrucao);
}

function altoConsumoDiscoFunc(banco, agencia, funcionario) {
  var instrucao = `
  SELECT TOP 1 Registro.capacidadeMax AS TotalCapacidade,
  Registro.usoAtual AS ConsumoAtual,
  FORMAT(Registro.dataHora, 'HH:mm:ss') AS DataHora
FROM Registro 
JOIN Alerta ON Registro.fkAlerta = Alerta.idAlerta
JOIN Maquina ON Registro.fkMaquina = Maquina.idMaquina
JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
WHERE causa = 'Erro de disco rígido'
AND gravidade IN ('Alta', 'Media', 'Baixa')
AND Maquina.fkFuncionario = ${funcionario}
AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
ORDER BY (Registro.usoAtual - Registro.capacidadeMax) DESC;
  `;

  return database.executar(instrucao);
}

function listarConsumoMaquina(funcionario) {

  var instrucao = `
  SELECT *
  FROM Maquina
  JOIN Funcionario ON Maquina.fkFuncionario = Funcionario.idFuncionario  
  WHERE Funcionario.idFuncionario = ${funcionario};
  `

  return database.executar(instrucao);
}

function ultimosRegistros(banco, agencia, funcionario) {
  var instrucao = `
  SELECT TOP 6
    Componente.idComponente AS IdComponente,
    Componente.nome AS NomeComponente,
    Registro.capacidadeMax AS CapacidadeMaxima,
    Registro.usoAtual AS UsoAtual,
    FORMAT(Registro.dataHora, 'HH:mm:ss') AS DataHora
FROM Registro
INNER JOIN Componente ON Registro.fkComponente = Componente.idComponente
INNER JOIN Maquina ON Registro.fkMaquina = Maquina.idMaquina
INNER JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
WHERE 
    Maquina.idMaquina IN (
        SELECT idMaquina 
        FROM Maquina 
        WHERE fkFuncionario = (SELECT idFuncionario FROM Funcionario WHERE idFuncionario = ${funcionario})
    )
    AND Maquina.fkFuncionario = (SELECT idFuncionario FROM Funcionario WHERE idFuncionario = ${funcionario})
    AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}');
    `;

  return database.executar(instrucao);
}

function ultimasTarefas(banco, agencia, funcionario) {
  var instrucao = `
  SELECT DISTINCT TOP 5
    Componente.nome AS NomeComponente,
    Registro.capacidadeMax AS CapacidadeMaxima,
    Processo.nome AS NomeProcesso,
    Processo.usoAtualRAM AS UsoAtualRAM,
    Processo.usoAtualDisco AS UsoAtualDisco,
    Processo.usoAtualCPU AS UsoAtualCPU,
    FORMAT(Processo.dataHora, 'HH:mm:ss') AS DataHora
FROM Processo
INNER JOIN Maquina ON Processo.fkMaquina = Maquina.idMaquina
INNER JOIN Registro ON Maquina.idMaquina = Registro.fkMaquina
INNER JOIN Componente ON Registro.fkComponente = Componente.idComponente
INNER JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
WHERE 
    Maquina.idMaquina IN (
        SELECT idMaquina 
        FROM Maquina 
        WHERE fkFuncionario = (SELECT idFuncionario FROM Funcionario WHERE idFuncionario = ${funcionario})
    )
    AND Maquina.fkFuncionario = (SELECT idFuncionario FROM Funcionario WHERE idFuncionario = ${funcionario})
    AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
ORDER BY 
    Processo.usoAtualRAM,
    Processo.usoAtualDisco,
    Processo.usoAtualCPU DESC;
    `;

  return database.executar(instrucao);
}

function verificarAgilidade(banco, agencia, funcionario) {
  var instrucao = `
  SELECT
    Componente.idComponente AS IdComponente,
    Componente.nome AS NomeComponente,
    AVG(Registro.capacidadeMax) AS CapacidadeMaxima,
    AVG(Registro.usoAtual) AS UsoAtual
FROM Registro
INNER JOIN Componente ON Registro.fkComponente = Componente.idComponente
INNER JOIN Maquina ON Registro.fkMaquina = Maquina.idMaquina
INNER JOIN Agencia ON Maquina.fkAgencia = Agencia.idAgencia
WHERE 
    Maquina.idMaquina IN (
        SELECT idMaquina 
        FROM Maquina 
        WHERE fkFuncionario = (SELECT idFuncionario FROM Funcionario WHERE idFuncionario = ${funcionario})
    ) 
    AND (Componente.idComponente = 1 OR Componente.idComponente = 2)
    AND Maquina.fkFuncionario = (SELECT idFuncionario FROM Funcionario WHERE idFuncionario = ${funcionario})
    AND Agencia.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}')
GROUP BY Componente.idComponente, Componente.nome
ORDER BY Componente.idComponente;
`
  return database.executar(instrucao);
}

function listarFuncionarios(agencia, banco) {
  var instrucao = `
  SELECT 
    Funcionario.nome AS NomeFuncionario,
    Funcionario.emailCorporativo AS EmailFuncionario,
    Funcionario.telefone AS TelefoneFuncionario,
    Funcionario.situacao AS SituacaoFuncionario,
    Maquina.idMaquina AS NumeracaoMaquina
  FROM Funcionario 
  JOIN Maquina ON Maquina.fkFuncionario = Funcionario.idFuncionario
  WHERE Funcionario.fkAgencia = ${agencia}
  AND Funcionario.fkEmpresa = (SELECT idEmpresa FROM Empresa WHERE nomeEmpresa = '${banco}');
`
  return database.executar(instrucao);
}


module.exports = {
  listarFuncionarios,
  listarProcessos,
  listarMaquinasAg,
  listarAgenciasNOC,
  listarMaquinas,
  altoConsumoCPU,
  altoConsumoRAM,
  altoConsumoDisco,
  statusMaquinas,
  listarLocalizacao,
  altoConsumoCPUFunc,
  altoConsumoRAMFunc,
  altoConsumoDiscoFunc,
  listarConsumoMaquina,
  ultimosRegistros,
  ultimasTarefas,
  verificarAgilidade,
}