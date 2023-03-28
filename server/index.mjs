import { WebSocketServer } from 'ws';
import { local } from './local.mjs';

const wss = new WebSocketServer({ port: 4001 });

const key = local.BIUA_KEY;

let motionLevel = 0;

function project(s1, t1, s2, t2, v) {
  return t1 + ((v - s1) * (t2 - t1)) / (s2 - s1);
}

const wsClients = [];

setInterval(() => {
  motionLevel = motionLevel * 0.9;
  console.log(motionLevel);
  wsClients.forEach(ws => ws.send(motionLevel));
}, 1000);

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
        wsClients.push(ws);
      }
    } else {
      if (!loggedIn) {
        return;
      }
      if (action === 'DIF') {
        const diff = Number.parseFloat(dataRest);
        const add = project(5.0, 0.0, 20.0, 0.2, diff);
        motionLevel = Math.min(motionLevel + add, 1.0);
        console.log('diff: ' + diff);
      }
    }
  });
});