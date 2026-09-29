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
INSERT INTO bot_types (name)
SELECT 'Chatbot'
WHERE NOT EXISTS (
    SELECT 1 FROM bot_types WHERE name = 'Chatbot'
);

INSERT INTO bot_types (name)
SELECT 'Assistant'
WHERE NOT EXISTS (
    SELECT 1 FROM bot_types WHERE name = 'Assistant'
);

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

-- Insertar Asistente de Prueba
INSERT INTO assistans (ia_provider, url, token, model, system_prompt, company_id)
SELECT 0, 'https://generativelanguage.googleapis.com/v1beta/openai/', 'AIzaSyDemoKeyForGemini', 'gemini-1.5-flash', 'Eres un asistente médico virtual útil, educado y profesional.', c.id
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1
      FROM assistans a
      WHERE a.company_id = c.id
  );

-- Insertar Usuario de Prueba
INSERT INTO users (username, password, role, company_id)
SELECT 'admin@demo.com', '123456', 'admin', c.id
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1
      FROM users u
      WHERE u.username = 'admin@demo.com'
  );
