
const express = require('express');

let admin = require("firebase-admin");
let serviceAccount = require('./serviceAccountKey.json')

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://final-9ea14-default-rtdb.asia-southeast1.firebasedatabase.app"
});

const app = express();
const port = 3000;

app.use(express.json());



const adminUid = 'nmhpvMnaEthBlENTTCMlhD9S4om2';
admin.auth().setCustomUserClaims(adminUid, { admin: true })
    .then(() => {
        console.log('Admin claim set for the user:', adminUid);
    })
    .catch(error => {
        console.error('Error setting admin claim:', error);
    });



app.get('/users', async (req, res, next) => {
    try {
        const listUsersResult = await admin.auth().listUsers();
        const users = listUsersResult.users
        .filter(user => !user.customClaims || !user.customClaims.admin)
        .map((user) => {
            const userData = user.toJSON();

            userData.lastSignInTime = user.metadata.lastSignInTime;
            userData.creationTime = user.metadata.creationTime;

            return userData;
        });

        res.status(200).json(users);
    } catch (error) {
        console.error('Error listing users:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
})

app.put('/user/:id', async (req, res, next) => {
    try {
        let userId = req.params.id;
        let { email, phoneNumber, displayName } = req.body;
        const updatedUser = await admin.auth().updateUser(userId, {
            email,
            phoneNumber,
            displayName,
            emailVerified: true,
        });

        console.log('Successfully updated user', updatedUser.toJSON());

        res.status(200).json({ success: true, message: 'User updated successfully' });
    } catch (error) {
        console.error('Error listing users:', error);
        res.status(500).json({ error: error.message });
    }
})

app.delete('/users/:id', (req, res, next) => {
    let { id } = req.params;
    admin.auth().deleteUser(id)
        .then(() => {
            res.status(200).json({ success: true });
        })
        .catch((error) => {
            console.log('Error deleting user:', error);
            res.status(500).json({ error: error.message });
        });

})

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});