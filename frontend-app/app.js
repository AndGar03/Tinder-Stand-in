(function () {
  const BASE_USUARIOS = "http://localhost:8081";
  const BASE_SOCIAL = "http://localhost:8082";
  const BASE_MULTIMEDIA = "http://localhost:8083";

  function setMensaje(id, texto, tipo) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = texto || "";
    el.classList.remove("ok", "error");
    if (tipo) {
      el.classList.add(tipo);
    }
  }

  function formToJson(form) {
    const data = new FormData(form);
    const obj = {};
    data.forEach((v, k) => {
      obj[k] = v;
    });
    return obj;
  }

  async function fetchJson(url, options) {
    const response = await fetch(url, options);
    const contentType = response.headers.get("content-type") || "";

    if (!contentType.includes("application/json")) {
      return { ok: response.ok, status: response.status };
    }

    const data = await response.json();
    return { ok: response.ok, status: response.status, data };
  }

  function initAuthPage() {
    const formRegistro = document.getElementById("form-registro");
    const formLogin = document.getElementById("form-login");
    const infoUsuario = document.getElementById("info-usuario");

    if (formRegistro) {
      formRegistro.addEventListener("submit", async (e) => {
        e.preventDefault();
        setMensaje("mensaje-registro", "Enviando registro...", "");

        const body = formToJson(formRegistro);
        if (body.password !== body.confirmPassword) {
          setMensaje("mensaje-registro", "Las contraseñas no coinciden", "error");
          return;
        }

        const payload = {
          nombreCompleto: body.nombreCompleto,
          email: body.email,
          telefono: body.telefono,
          username: body.username,
          password: body.password,
        };

        try {
          const res = await fetchJson(`${BASE_USUARIOS}/api/auth/registro`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
          });

          if (res.ok) {
            setMensaje("mensaje-registro", "Usuario registrado exitosamente", "ok");
            formRegistro.reset();
          } else {
            const msg = res.data && res.data.mensaje
              ? res.data.mensaje
              : "Error al registrar usuario";
            setMensaje("mensaje-registro", msg, "error");
          }
        } catch (err) {
          setMensaje("mensaje-registro", "Error de conexión con ms-usuarios", "error");
        }
      });
    }

    if (formLogin) {
      formLogin.addEventListener("submit", async (e) => {
        e.preventDefault();
        setMensaje("mensaje-login", "Iniciando sesión...", "");

        const body = formToJson(formLogin);
        const payload = {
          username: body.username,
          password: body.password,
        };

        try {
          const res = await fetchJson(`${BASE_USUARIOS}/api/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
          });

          if (res.ok && res.data) {
            setMensaje("mensaje-login", "Login exitoso", "ok");
            if (infoUsuario) {
              infoUsuario.classList.remove("oculto");
              const d = res.data;
              infoUsuario.innerHTML =
                `<p><strong>ID:</strong> ${d.id}</p>` +
                `<p><strong>Usuario:</strong> ${d.username}</p>` +
                `<p><strong>Email:</strong> ${d.email}</p>`;
            }
          } else {
            const msg = res.data && res.data.mensaje
              ? res.data.mensaje
              : "Credenciales inválidas";
            setMensaje("mensaje-login", msg, "error");
          }
        } catch (err) {
          setMensaje("mensaje-login", "Error de conexión con ms-usuarios", "error");
        }
      });
    }
  }

  function initPerfilesPage() {
    const formLike = document.getElementById("form-like");
    const formMatches = document.getElementById("form-matches");
    const formLikesRecibidos = document.getElementById("form-likes-recibidos");

    if (formLike) {
      formLike.addEventListener("submit", async (e) => {
        e.preventDefault();
        setMensaje("mensaje-like", "Enviando like...", "");

        const body = formToJson(formLike);
        const usuarioOrigenId = Number(body.usuarioOrigenId);
        const usuarioDestinoId = Number(body.usuarioDestinoId);

        try {
          const res = await fetchJson(
            `${BASE_SOCIAL}/api/social/likes?usuarioOrigenId=${usuarioOrigenId}`,
            {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ usuarioDestinoId }),
            }
          );

          if (res.ok && res.data) {
            const texto = res.data.esMatch
              ? "Like enviado y se generó un match"
              : "Like enviado";
            setMensaje("mensaje-like", texto, "ok");
          } else {
            const msg = res.data && res.data.mensaje
              ? res.data.mensaje
              : "Error al enviar like";
            setMensaje("mensaje-like", msg, "error");
          }
        } catch (err) {
          setMensaje("mensaje-like", "Error de conexión con ms-social", "error");
        }
      });
    }

    if (formMatches) {
      formMatches.addEventListener("submit", async (e) => {
        e.preventDefault();
        const body = formToJson(formMatches);
        const usuarioId = Number(body.usuarioId);
        const contenedor = document.getElementById("lista-matches");
        if (!contenedor) return;
        contenedor.innerHTML = "Cargando matches...";

        try {
          const res = await fetchJson(`${BASE_SOCIAL}/api/social/matches/${usuarioId}`);
          if (res.ok && Array.isArray(res.data)) {
            if (res.data.length === 0) {
              contenedor.textContent = "Sin matches por ahora";
              return;
            }
            contenedor.innerHTML = "";
            res.data.forEach((m) => {
              const div = document.createElement("div");
              div.className = "item";
              div.textContent = `Match #${m.id} entre ${m.usuario1Id} y ${m.usuario2Id}`;
              contenedor.appendChild(div);
            });
          } else {
            contenedor.textContent = "Error al obtener matches";
          }
        } catch (err) {
          const contenedor2 = document.getElementById("lista-matches");
          if (contenedor2) {
            contenedor2.textContent = "Error de conexión con ms-social";
          }
        }
      });
    }

    if (formLikesRecibidos) {
      formLikesRecibidos.addEventListener("submit", async (e) => {
        e.preventDefault();
        const body = formToJson(formLikesRecibidos);
        const usuarioId = Number(body.usuarioId);
        const contenedor = document.getElementById("lista-likes");
        if (!contenedor) return;
        contenedor.innerHTML = "Cargando likes...";

        try {
          const res = await fetchJson(
            `${BASE_SOCIAL}/api/social/likes/recibidos/${usuarioId}`
          );
          if (res.ok && Array.isArray(res.data)) {
            if (res.data.length === 0) {
              contenedor.textContent = "Aún no tienes likes";
              return;
            }
            contenedor.innerHTML = "";
            res.data.forEach((l) => {
              const div = document.createElement("div");
              div.className = "item";
              div.textContent = `Like #${l.id} de ${l.usuarioOrigenId}`;
              contenedor.appendChild(div);
            });
          } else {
            contenedor.textContent = "Error al obtener likes";
          }
        } catch (err) {
          const contenedor2 = document.getElementById("lista-likes");
          if (contenedor2) {
            contenedor2.textContent = "Error de conexión con ms-social";
          }
        }
      });
    }
  }

  function initFotosPage() {
    const formFoto = document.getElementById("form-foto");
    const formFotosUsuario = document.getElementById("form-fotos-usuario");

    if (formFoto) {
      formFoto.addEventListener("submit", async (e) => {
        e.preventDefault();
        setMensaje("mensaje-foto", "Guardando foto...", "");

        const body = formToJson(formFoto);
        const payload = {
          url: body.url,
          usuarioId: Number(body.usuarioId),
        };

        try {
          const res = await fetchJson(`${BASE_MULTIMEDIA}/api/fotos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
          });

          if (res.ok) {
            setMensaje("mensaje-foto", "Foto registrada", "ok");
          } else {
            setMensaje("mensaje-foto", "Error al registrar foto", "error");
          }
        } catch (err) {
          setMensaje("mensaje-foto", "Error de conexión con ms-multimedia", "error");
        }
      });
    }

    if (formFotosUsuario) {
      formFotosUsuario.addEventListener("submit", async (e) => {
        e.preventDefault();
        const body = formToJson(formFotosUsuario);
        const usuarioId = Number(body.usuarioId);
        const galeria = document.getElementById("galeria-fotos");
        if (!galeria) return;
        galeria.innerHTML = "Cargando fotos...";

        try {
          const res = await fetchJson(
            `${BASE_MULTIMEDIA}/api/fotos/usuario/${usuarioId}`
          );
          if (res.ok && Array.isArray(res.data)) {
            if (res.data.length === 0) {
              galeria.textContent = "Este usuario no tiene fotos";
              return;
            }
            galeria.innerHTML = "";
            res.data.forEach((f) => {
              const fig = document.createElement("figure");
              const img = document.createElement("img");
              img.src = f.url;
              img.alt = "Foto de usuario";
              const cap = document.createElement("figcaption");
              cap.textContent = `ID ${f.id} - ${f.fechaCreacion || ""}`;
              fig.appendChild(img);
              fig.appendChild(cap);
              galeria.appendChild(fig);
            });
          } else {
            galeria.textContent = "Error al obtener fotos";
          }
        } catch (err) {
          const galeria2 = document.getElementById("galeria-fotos");
          if (galeria2) {
            galeria2.textContent = "Error de conexión con ms-multimedia";
          }
        }
      });
    }
  }

  document.addEventListener("DOMContentLoaded", () => {
    const bodyId = document.body.id;
    if (bodyId === "pagina-auth") {
      initAuthPage();
    } else if (bodyId === "pagina-perfiles") {
      initPerfilesPage();
    } else if (bodyId === "pagina-fotos") {
      initFotosPage();
    }
  });
})();
