var express = require("express");
var router = express.Router();

var usuarioController = require("../controllers/usuarioController");

//Recebendo os dados do html e direcionando para a função cadastrar de usuarioController.js
router.post("/cadastrarFuncionario", function (req, res) {
    usuarioController.cadastrarFuncionario(req, res);
})

router.post("/cadastrarEndAgencia", function (req, res) {
    usuarioController.cadastrarEndAgencia(req, res);
})

router.post("/cadastrarAgencia", function(req, res) {
    usuarioController.cadastrarAgencia(req, res);
})

router.post("/autenticar", function (req, res) {
    usuarioController.autenticar(req, res);
});

router.post("/listarFuncionario", function (req, res) {
    usuarioController.listarFuncionario(req, res);
});

router.post("/listarEmpresa", function (req, res) {
    usuarioController.listarEmpresa(req, res);
});

router.post("/cadastrarMaquina", function (req, res) {
    usuarioController.cadastrarMaquina(req, res);
});

router.post("/cadastrarToken", function (req, res) {
    usuarioController.cadastrarToken(req, res);
});


module.exports = router;