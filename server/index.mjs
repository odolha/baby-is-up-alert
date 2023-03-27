import { WebSocketServer } from 'ws';
import { local } from './local.mjs';

const wss = new WebSocketServer({ port: 4001 });

const key = local.BIUA_KEY;

wss.on('connection', function connection(ws) {
  console.error('Incoming ' + ws._socket.remoteAddress);

  let loggedIn = false;
  setTimeout(() => {
    if (!loggedIn) {
      console.error('Missing login. Disconnecting. ' + ws._socket.remoteAddress);
      ws.close();
    }
  }, 5000);

  ws.on('error', console.error);

  ws.on('message', function message(data) {
    const dataStr = data.toString();
    const action = dataStr.slice(0, 3);
    const dataRest = dataStr.slice(3);

    if (action === 'LOG') {
      if (dataRest !== key) {
        console.error('Invalid key. ' + ws._socket.remoteAddress);
      } else {
        console.info('Logged in ' + ws._socket.remoteAddress);
        loggedIn = true;
      }
    } else {
      if (!loggedIn) {
        return;
      }
      if (action === 'DIF') {
        const diff = Number.parseFloat(dataRest);
        console.log('DIF ' + diff);
      }
    }
  });
});