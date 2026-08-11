INSERT INTO companies (name, webhook_token, configs)
SELECT 'Demo Clinic', 'demo-webhook-token', '{}'::jsonb
WHERE NOT EXISTS (
    SELECT 1
    FROM companies
    WHERE name = 'Demo Clinic'
);

INSERT INTO contacts (name, company_id, number, has_notification)
SELECT 'Contacto Demo', c.id, '999999999', false
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1
      FROM contacts ct
      WHERE ct.number = '999999999'
  );

INSERT INTO messages (text, is_from_contact, is_from_company, company_id, contact_id)
SELECT 'Mensaje de prueba para validar el flujo', true, false, c.id, ct.id
FROM companies c
JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic'
  AND ct.number = '999999999'
  AND NOT EXISTS (
      SELECT 1
      FROM messages m
      WHERE m.contact_id = ct.id
        AND m.text = 'Mensaje de prueba para validar el flujo'
  );

-- Insertar Bot Types
INSERT INTO bot_types (name, description)
VALUES ('Chatbot', 'Bot de atención al cliente')
ON CONFLICT DO NOTHING;

INSERT INTO bot_types (name, description)
VALUES ('Assistant', 'Asistente inteligente')
ON CONFLICT DO NOTHING;

-- Insertar Bot de Prueba
INSERT INTO bots (name, components, trigger, company_id, bot_type_id)
SELECT 'Bot Demo', '{}'::jsonb, 'hello', c.id, bt.id
FROM companies c, bot_types bt
WHERE c.name = 'Demo Clinic' 
  AND bt.name = 'Chatbot'
  AND NOT EXISTS (
      SELECT 1
      FROM bots b
      WHERE b.name = 'Bot Demo' AND b.company_id = c.id
  );

-- Insertar Asistentes de Prueba
INSERT INTO assistans (name, description, company_id)
SELECT 'Asistente Médico', 'Asistente para consultas médicas', c.id
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1
      FROM assistans a
      WHERE a.name = 'Asistente Médico' AND a.company_id = c.id
  );