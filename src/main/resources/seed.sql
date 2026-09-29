-- ==========================================================
-- SEED DATA FOR CHATQUARKUS - FRONTEND TESTING & DEMO
-- ==========================================================

-- 1. COMPANIES
INSERT INTO companies (name, webhooktoken, email)
SELECT 'Demo Clinic', 'MDm9WKPcEVFIn4lgJzBaUZsQwBczAtSB', 'admin@democlinic.com'
WHERE NOT EXISTS (
    SELECT 1 FROM companies WHERE name = 'Demo Clinic'
);

INSERT INTO companies (name, webhooktoken, email)
SELECT 'Dental Plus', 'dental-plus-webhook-token', 'contacto@dentalplus.com'
WHERE NOT EXISTS (
    SELECT 1 FROM companies WHERE name = 'Dental Plus'
);

-- 2. USERS (Password for all: '123456')
-- BCrypt Hash: $2a$10$dAYJX/hzmw4cJogkdopGrOwGy5QYfxXsRnqcKEeWP25BfEPjUU6Yy
INSERT INTO users (username, password, role, company_id)
SELECT 'admin@demo.com', '$2a$10$dAYJX/hzmw4cJogkdopGrOwGy5QYfxXsRnqcKEeWP25BfEPjUU6Yy', 'admin', c.id
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1 FROM users WHERE username = 'admin@demo.com'
  );

INSERT INTO users (username, password, role, company_id)
SELECT 'agente@demo.com', '$2a$10$dAYJX/hzmw4cJogkdopGrOwGy5QYfxXsRnqcKEeWP25BfEPjUU6Yy', 'agent', c.id
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1 FROM users WHERE username = 'agente@demo.com'
  );

INSERT INTO users (username, password, role, company_id)
SELECT 'admin@dentalplus.com', '$2a$10$dAYJX/hzmw4cJogkdopGrOwGy5QYfxXsRnqcKEeWP25BfEPjUU6Yy', 'admin', c.id
FROM companies c
WHERE c.name = 'Dental Plus'
  AND NOT EXISTS (
      SELECT 1 FROM users WHERE username = 'admin@dentalplus.com'
  );

-- 3. AI ASSISTANTS (iaprovider: 0=OPEN_AI, 1=GEMINI, 2=DEEPSEK)
INSERT INTO assistans (iaprovider, url, token, model, systemprompt, toolsjson, documentsjson, company_id)
SELECT
    1,
    'https://generativelanguage.googleapis.com/v1beta/openai/',
    'AIzaSyDemoKeyForGemini',
    'gemini-1.5-flash',
    'Eres Sofía, la asistente virtual médica de Demo Clinic. Tu propósito es orientar a los pacientes, brindar información sobre especialidades, agendar citas y resolver dudas frecuentes con amabilidad y precisión.',
    '[
      {
        "name": "consultar_citas_disponibles",
        "description": "Consulta horarios y fechas disponibles para citas con especialistas.",
        "script": "async function execute(args) {\n  console.log(\"Consultando citas para:\", args.specialty || \"General\");\n  return {\n    available: true,\n    specialty: args.specialty || \"Medicina General\",\n    date: \"2026-04-05\",\n    slots: [\"08:30 AM\", \"10:00 AM\", \"02:30 PM\", \"04:00 PM\"]\n  };\n}"
      },
      {
        "name": "cotizar_servicio_medico",
        "description": "Calcula el costo aproximado del servicio médico o consulta solicitada.",
        "script": "async function execute(args) {\n  console.log(\"Calculando costo para:\", args.service);\n  const service = (args.service || \"\").toLowerCase();\n  let cost = 30000;\n  if (service.includes(\"dental\") || service.includes(\"odontolog\")) cost = 45000;\n  else if (service.includes(\"especial\") || service.includes(\"cardio\")) cost = 60000;\n  else if (service.includes(\"laboratorio\") || service.includes(\"sangre\")) cost = 25000;\n  return {\n    service: args.service || \"Consulta General\",\n    cost: cost,\n    currency: \"CRC\",\n    taxIncluded: true\n  };\n}"
      },
      {
        "name": "consultar_estado_sede",
        "description": "Obtiene el estado de atención y tiempo de espera en una sede física.",
        "script": "async function execute(args) {\n  console.log(\"Verificando sede:\", args.location);\n  return {\n    location: args.location || \"Sede Central\",\n    isOpen: true,\n    estimatedWaitTimeMinutes: 10,\n    emergencyServiceAvailable: true\n  };\n}"
      }
    ]'::jsonb,
    '[
      {
        "id": 1,
        "fileName": "Guia_Servicios_Medicos_2026.pdf",
        "fileUrl": "https://storage.googleapis.com/demo-docs/Guia_Servicios_Medicos_2026.pdf",
        "status": "INDEXED"
      },
      {
        "id": 2,
        "fileName": "Politicas_Cancelacion_y_Reprogramacion.pdf",
        "fileUrl": "https://storage.googleapis.com/demo-docs/Politicas_Cancelacion_y_Reprogramacion.pdf",
        "status": "INDEXED"
      },
      {
        "id": 3,
        "fileName": "Preparacion_Examenes_Laboratorio.pdf",
        "fileUrl": "https://storage.googleapis.com/demo-docs/Preparacion_Examenes_Laboratorio.pdf",
        "status": "INDEXED"
      }
    ]'::jsonb,
    c.id
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1 FROM assistans WHERE company_id = c.id
  );

INSERT INTO assistans (iaprovider, url, token, model, systemprompt, toolsjson, documentsjson, company_id)
SELECT
    0,
    'https://api.openai.com/v1',
    'sk-demo-openai-key',
    'gpt-4o-mini',
    'Eres el asistente virtual de Dental Plus. Ayudas a los clientes a agendar limpiezas, ortodoncia e implantes dentales.',
    '[]'::jsonb,
    '[]'::jsonb,
    c.id
FROM companies c
WHERE c.name = 'Dental Plus'
  AND NOT EXISTS (
      SELECT 1 FROM assistans WHERE company_id = c.id
  );

-- 4. CONTACTS (For Demo Clinic)
INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'Carlos Mendoza', c.id, '50688881111', true, 'carlos.mendoza@gmail.com', NOW() - INTERVAL '5 minutes'
FROM companies c WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688881111' AND company_id = c.id);

INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'Dra. Elena Ramos', c.id, '50688882222', false, 'elena.ramos@salud.cr', NOW() - INTERVAL '25 minutes'
FROM companies c WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688882222' AND company_id = c.id);

INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'Andrés Morales', c.id, '50688883333', true, 'andres.morales@outlook.com', NOW() - INTERVAL '1 hour'
FROM companies c WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688883333' AND company_id = c.id);

INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'Valeria Castillo', c.id, '50688884444', false, 'valeria.castillo@yahoo.com', NOW() - INTERVAL '3 hours'
FROM companies c WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688884444' AND company_id = c.id);

INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'Roberto Sánchez', c.id, '50688885555', false, 'roberto.sanchez@gmail.com', NOW() - INTERVAL '1 day'
FROM companies c WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688885555' AND company_id = c.id);

INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'María José Navarro', c.id, '50688886666', true, 'mj.navarro@gmail.com', NOW() - INTERVAL '2 days'
FROM companies c WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688886666' AND company_id = c.id);

-- Contact for Dental Plus
INSERT INTO contacts (name, company_id, number, hasnotification, email, lastinteraction)
SELECT 'Gabriel Vega', c.id, '50688887777', false, 'gabi.vega@gmail.com', NOW() - INTERVAL '4 hours'
FROM companies c WHERE c.name = 'Dental Plus'
  AND NOT EXISTS (SELECT 1 FROM contacts WHERE number = '50688887777' AND company_id = c.id);

-- 5. MESSAGES / CHAT HISTORIES

-- Chat 1: Carlos Mendoza (Solicitud de cita)
INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT '¡Hola! Buenas tardes, quisiera consultar los horarios disponibles para medicina general esta semana.', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688881111'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE '¡Hola! Buenas tardes, quisiera consultar%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT '¡Buenas tardes Carlos! Con gusto te ayudo. Para medicina general tenemos espacios disponibles el jueves a las 10:00 AM y el viernes a las 2:30 PM. ¿Cuál te queda mejor?', false, true, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688881111'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE '¡Buenas tardes Carlos! Con gusto%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'El viernes a las 2:30 PM me queda perfecto. ¿Qué costo tiene la consulta?', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688881111'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'El viernes a las 2:30 PM%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'La consulta general tiene un costo de ₡30,000 colones (IVA incluido). Te agendé para el viernes a las 2:30 PM con el Dr. Quesada. ¿Deseas agregar algún examen de laboratorio?', false, true, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688881111'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'La consulta general tiene un costo%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Por ahora solo la consulta, muchas gracias.', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688881111'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Por ahora solo la consulta%');

-- Chat 2: Dra. Elena Ramos (Resultados de Laboratorio)
INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Hola, buenos días. ¿Ya están listos los resultados de laboratorio de mi hemograma?', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688882222'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Hola, buenos días. ¿Ya están listos%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Hola Dra. Elena. Sí, ya fueron cargados en el sistema y enviados a su correo electrónico registrado.', false, true, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688882222'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Hola Dra. Elena. Sí, ya fueron%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Excelente, ya los recibí. Muchas gracias por la pronta atención.', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688882222'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Excelente, ya los recibí.%');

-- Chat 3: Andrés Morales (Cotización de Cirugía Menor)
INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Buenas, me gustaría saber si realizan suturas y pequeñas cirugías ambulatorias y cuál es el procedimiento.', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688883333'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Buenas, me gustaría saber si realizan%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Hola Andrés. Sí realizamos procedimientos ambulatorios. Primero se requiere una valoración previa con el médico especialista para determinar el procedimiento adecuado.', false, true, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688883333'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Hola Andrés. Sí realizamos procedimientos%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT '¿Cuánto cuesta la cita de valoración?', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688883333'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE '¿Cuánto cuesta la cita de valoración?');

-- Chat 4: Valeria Castillo (Confirmación y Ubicación)
INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Hola, ¿dónde queda ubicada la sede central?', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688884444'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Hola, ¿dónde queda ubicada%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Hola Valeria, estamos ubicados 200 metros norte del Hospital Nacional, edificio Torre Médica, piso 3.', false, true, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic' AND ct.number = '50688884444'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Hola Valeria, estamos ubicados%');

-- Chat 5: Gabriel Vega (Dental Plus)
INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT 'Hola Dental Plus, quisiera agendar una cita para limpieza dental.', true, false, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Dental Plus' AND ct.number = '50688887777'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE 'Hola Dental Plus, quisiera agendar%');

INSERT INTO messages (text, isfromcontact, isfromcompany, company_id, contact_id)
SELECT '¡Hola Gabriel! Con gusto. Tenemos disponible este sábado a las 9:00 AM para tu profilaxis dental.', false, true, c.id, ct.id
FROM companies c JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Dental Plus' AND ct.number = '50688887777'
  AND NOT EXISTS (SELECT 1 FROM messages WHERE contact_id = ct.id AND text LIKE '¡Hola Gabriel! Con gusto.%');
