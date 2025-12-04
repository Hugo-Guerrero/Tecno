import { onCall, onRequest, CallableRequest } from "firebase-functions/v2/https";
import { onDocumentUpdated, onDocumentCreated } from "firebase-functions/v2/firestore";
import * as admin from "firebase-admin";
import { FieldValue } from "firebase-admin/firestore"; // Importar FieldValue

// Inicialización segura
admin.initializeApp();
const db = admin.firestore();
const messaging = admin.messaging();
const REGION = "us-central1";

/**
 * FUNCIÓN 1: Crear Preferencia de Pago (HTTPS Callable) - LEGACY
 */
// ... (mantenemos las funciones de Mercado Pago por si se reutilizan en el futuro)

/**
 * FUNCIÓN 2: Webhook para Notificaciones de MP (HTTPS) - LEGACY
 */
// ...

/**
 * FUNCIÓN 3: Trigger Firestore para notificar al donante
 */
export const onDonationUpdate = onDocumentUpdated(
  "donations/{donationId}",
  async (event) => {
    try {
      const before = event.data?.before?.data();
      const after = event.data?.after?.data();
      if (!before || !after) return;

      // --- LÓGICA DE NOTIFICACIÓN --- 
      if (before.status !== after.status && after.status === "COMPLETED") {
        const donorProfileDoc = await db.collection("users").doc(after.donorUid).get();
        const donorProfile = donorProfileDoc.exists ? donorProfileDoc.data() : null;
        if (donorProfile?.fcmToken) {
            const projectDoc = await db.collection("projects").doc(after.projectId).get();
            const projectTitle = projectDoc.exists ? projectDoc.data()?.title : "tu proyecto";

            const body = `¡Tu donación de ${after.amount} para '${projectTitle}' fue aprobada con éxito!`;
            const message = {
                token: donorProfile.fcmToken,
                notification: { title: "¡Donación Aprobada!", body },
            };
            await messaging.send(message);
            console.log("Notificación de aprobación enviada al donante.");
        }
      }

      // --- LÓGICA PARA ACTUALIZAR EL PROGRESO --- 
      if (before.status === "PENDING" && after.status === "COMPLETED") {
        const projectRef = db.collection("projects").doc(after.projectId);
        await projectRef.update({
            currentAmount: FieldValue.increment(after.amount)
        });
        console.log(`Proyecto ${after.projectId} actualizado con ${after.amount}`);
      }

    } catch (e) {
      console.error("Error en onDonationUpdate:", e);
    }
  }
);
