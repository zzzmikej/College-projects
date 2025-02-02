// require('dotenv').config();
// const { App } = require('@slack/bolt');

// const slackSigningSecret = process.env.SLACK_SIGNING_SECRET;
// const slackToken = process.env.SLACK_BOT_TOKEN;
// const port = process.env.PORT || 3333;

// const app = new App({
//   signingSecret: slackSigningSecret,
//   token: slackToken,
// });

// // Responda a eventos específicos do Slack aqui
// app.message('quote', async ({ message, say }) => {
//   // Lógica para responder a mensagens com 'quote'
//   say('Sua mensagem de resposta aqui');
// });

// // Função para enviar uma mensagem para um canal no Slack
// async function sendSlackMessage(message, channel) {
//   try {
//     await app.client.chat.postMessage({
//       channel: channel, // Substitua pelo ID do canal desejado
//       text: message,
//     });
//   } catch (error) {
//     console.error('Erro ao enviar mensagem para o Slack:', error);
//   }
// }

// sendSlackMessage('Sua mensagem aqui', 'C05N5Q0ST34'); // Substitua pelo ID do canal desejado

// (async () => {
//   await app.start(port);
//   console.log(`⚡️ Bolt app is running on port ${port}`);
// })();
