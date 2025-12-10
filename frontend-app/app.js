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

        const fechaNacimientoValor = body.fechaNacimiento || document.querySelector('#form-registro input[name="fechaNacimiento"]').value;

        const payload = {
          // Adaptar al DTO RegistroRequest del backend
          nombreCompleto: `${body.nombre} ${body.apellido}`.trim(),
          username: body.username,
          email: body.email,
          password: body.password,
          telefono: body.telefono && body.telefono.trim() !== "" ? body.telefono : null,
          fechaNacimiento: fechaNacimientoValor,
        };

        try {
          const res = await fetchJson(`${BASE_USUARIOS}/api/auth/registro`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
          });

          if (res.ok && res.data) {
            const datos = res.data;
            const mensaje = datos.mensaje || "Usuario registrado exitosamente";
            setMensaje("mensaje-registro", mensaje, "ok");

            const resumen = document.getElementById("registro-resumen");
            if (resumen) {
              resumen.classList.remove("oculto");
              resumen.innerHTML =
                `<p><strong>ID asignado:</strong> ${datos.id}</p>` +
                `<p><strong>Usuario:</strong> ${datos.username}</p>` +
                `<p><strong>Contraseña:</strong> ${datos.password}</p>`;
            }

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

  let swipingEstado = {
    usuarioActualId: null,
    likesRecibidos: [],
    indiceActual: 0,
  };

  async function cargarLikesParaSwiping(usuarioId) {
    setMensaje("mensaje-swiping", "Cargando perfiles...", "");
    const res = await fetchJson(`${BASE_SOCIAL}/api/social/likes/recibidos/${usuarioId}`);
    if (!res.ok || !Array.isArray(res.data) || res.data.length === 0) {
      setMensaje("mensaje-swiping", "No hay perfiles disponibles para swiping", "error");
      const tarjeta = document.getElementById("tarjeta-swiping");
      if (tarjeta) {
        tarjeta.classList.add("oculto");
      }
      swipingEstado.likesRecibidos = [];
      swipingEstado.indiceActual = 0;
      return;
    }
    swipingEstado.usuarioActualId = usuarioId;
    swipingEstado.likesRecibidos = res.data;
    swipingEstado.indiceActual = 0;
    await mostrarPerfilActual();
  }

  async function mostrarPerfilActual() {
    const tarjeta = document.getElementById("tarjeta-swiping");
    if (!tarjeta) return;

    if (!swipingEstado.likesRecibidos.length || swipingEstado.indiceActual >= swipingEstado.likesRecibidos.length) {
      tarjeta.classList.add("oculto");
      setMensaje("mensaje-swiping", "Has revisado todos los perfiles", "ok");
      return;
    }

    const like = swipingEstado.likesRecibidos[swipingEstado.indiceActual];
    const usuarioDestinoId = like.usuarioOrigenId;

    try {
      const resUsuario = await fetchJson(`${BASE_USUARIOS}/api/auth/usuario/${usuarioDestinoId}`);
      if (!resUsuario.ok || !resUsuario.data) {
        setMensaje("mensaje-swiping", "No se pudo cargar el perfil", "error");
        return;
      }
      const u = resUsuario.data;
      tarjeta.classList.remove("oculto");
      const nombre = document.getElementById("swipe-nombre");
      const bio = document.getElementById("swipe-bio");
      const ciudad = document.getElementById("swipe-ciudad");
      const email = document.getElementById("swipe-email");
      const fotosContainer = document.getElementById("swipe-fotos");

      if (nombre) nombre.textContent = u.nombreCompleto || `Usuario #${u.id}`;
      if (bio) bio.textContent = u.descripcion || "Sin descripción";
      if (ciudad) ciudad.textContent = u.ciudad ? `Ciudad: ${u.ciudad}` : "";
      if (email) email.textContent = u.email ? `Email: ${u.email}` : "";

      // Cargar fotos desde ms-multimedia. Si no hay fotos o falla, dejamos el contenedor vacío.
      if (fotosContainer) {
        fotosContainer.innerHTML = "";
        try {
          const resFotos = await fetchJson(
            `${BASE_MULTIMEDIA}/api/fotos/usuario/${usuarioDestinoId}`
          );
          if (resFotos.ok && Array.isArray(resFotos.data) && resFotos.data.length > 0) {
            resFotos.data.forEach((f) => {
              const img = document.createElement("img");
              img.src = f.url;
              img.alt = `Foto de ${u.nombreCompleto || "usuario"}`;
              fotosContainer.appendChild(img);
            });
          }
        } catch (e) {
          // Ignoramos errores de fotos; el swiping funciona igual solo con texto.
        }
      }

      setMensaje("mensaje-swiping", "", "");
    } catch (e) {
      setMensaje("mensaje-swiping", "Error al obtener el perfil", "error");
    }
  }

  async function procesarAccionSwiping(tipoAccion) {
    if (!swipingEstado.likesRecibidos.length || swipingEstado.indiceActual >= swipingEstado.likesRecibidos.length) {
      return;
    }
    const likeActual = swipingEstado.likesRecibidos[swipingEstado.indiceActual];
    const usuarioDestinoId = likeActual.usuarioOrigenId;

    if (tipoAccion === "like") {
      try {
        const res = await fetchJson(
          `${BASE_SOCIAL}/api/social/likes?usuarioOrigenId=${swipingEstado.usuarioActualId}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ usuarioDestinoId }),
          }
        );
        if (res.ok) {
          setMensaje("mensaje-swiping", "Like registrado", "ok");
        } else {
          setMensaje("mensaje-swiping", "No se pudo registrar el like", "error");
        }
      } catch (e) {
        setMensaje("mensaje-swiping", "Error de conexión con ms-social", "error");
      }
    } else {
      // Dislike: solo avanzar sin registrar nada en el backend
      setMensaje("mensaje-swiping", "Perfil descartado", "");
    }

    swipingEstado.indiceActual += 1;
    await mostrarPerfilActual();
  }

  function initPerfilesPage() {
    const formLike = document.getElementById("form-like");
    const formMatches = document.getElementById("form-matches");
    const formLikesRecibidos = document.getElementById("form-likes-recibidos");

    const btnIniciarSwiping = document.getElementById("btn-iniciar-swiping");
    const btnSwipeLike = document.getElementById("btn-swipe-like");
    const btnSwipeDislike = document.getElementById("btn-swipe-dislike");

    if (btnIniciarSwiping) {
      btnIniciarSwiping.addEventListener("click", async () => {
        const inputId = document.getElementById("swipe-usuario-id");
        if (!inputId || !inputId.value) {
          setMensaje("mensaje-swiping", "Debes indicar tu ID de usuario", "error");
          return;
        }
        const usuarioId = Number(inputId.value);
        if (!Number.isFinite(usuarioId) || usuarioId <= 0) {
          setMensaje("mensaje-swiping", "ID de usuario inválido", "error");
          return;
        }
        await cargarLikesParaSwiping(usuarioId);
      });
    }

    if (btnSwipeLike) {
      btnSwipeLike.addEventListener("click", () => {
        procesarAccionSwiping("like");
      });
    }

    if (btnSwipeDislike) {
      btnSwipeDislike.addEventListener("click", () => {
        procesarAccionSwiping("dislike");
      });
    }

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
