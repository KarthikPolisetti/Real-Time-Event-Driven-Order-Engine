import React, { useState, useEffect } from "react";
import axios from "axios";
import { Client } from "https://esm.sh/@stomp/stompjs";

export default function App() {
    // --- STATE ---
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [token, setToken] = useState('');
    const [loginUsername, setLoginUsername] = useState('');
    const [loginPassword, setLoginPassword] = useState('');

    const [notifications, setNotifications] = useState([]);
    const [isConnected, setIsConnected] = useState(false);
    const [message, setMessage] = useState("");

    // --- WEBSOCKET CONNECTION ---
    useEffect(() => {
        if (!isLoggedIn) return;

        const client = new Client({
            brokerURL: "ws://localhost:8080/ws/websocket", // Fixed URL!
            onConnect: () => {
                setIsConnected(true);
                client.subscribe("/topic/notifications", (msg) => {
                    setNotifications((prev) => [msg.body, ...prev].slice(0, 8));
                });
            },
            onDisconnect: () => setIsConnected(false),
            onWebSocketError: () => setIsConnected(false),
        });

        client.activate();
        return () => client.deactivate();
    }, [isLoggedIn]);

    // --- LOGIN FUNCTION ---
    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post('http://localhost:8080/api/auth/login', {
                username: loginUsername,
                password: loginPassword
            });
            setToken(res.data.token);
            setIsLoggedIn(true);
        } catch (err) {
            alert("Login Failed. Check credentials.");
        }
    };

    // --- ORDER FUNCTION ---
    const sendNotification = async (e) => {
        e.preventDefault();
        if (!message.trim()) return;
        try {
            // Flashing the VIP wristband to the Bouncer!
            await axios.post("http://localhost:8080/api/orders",
                { productName: message },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setMessage("");
        } catch (err) {
            console.error(err);
            alert("Failed to place order. Is the backend running?");
        }
    };

    // --- STYLES ---
    const styles = {
        loginContainer: { display: "flex", justifyContent: "center", alignItems: "center", minHeight: "100vh", backgroundColor: "#0f172a", fontFamily: "sans-serif" },
        loginCard: { backgroundColor: "#1e293b", padding: "40px", borderRadius: "16px", border: "1px solid #334155", width: "100%", maxWidth: "400px", textAlign: "center" },
        container: { maxWidth: "1024px", margin: "0 auto", padding: "40px", backgroundColor: "#0f172a", color: "#e2e8f0", minHeight: "100vh", fontFamily: "sans-serif" },
        header: { borderBottom: "1px solid #334155", paddingBottom: "24px", marginBottom: "32px", display: "flex", justifyContent: "space-between", alignItems: "center" },
        title: { fontSize: "2rem", fontWeight: "bold", color: "#ffffff", margin: 0 },
        badge: { display: "inline-block", padding: "6px 16px", borderRadius: "999px", fontSize: "0.75rem", fontWeight: "bold", border: "1px solid", borderColor: isConnected ? "#34d399" : "#f43f5e", color: isConnected ? "#34d399" : "#f43f5e", letterSpacing: "1px" },
        grid: { display: "grid", gridTemplateColumns: "1fr 2fr", gap: "32px" },
        card: { backgroundColor: "#1e293b", padding: "32px", borderRadius: "16px", border: "1px solid #334155" },
        input: { width: "100%", backgroundColor: "#0f172a", border: "1px solid #475569", borderRadius: "8px", padding: "14px", color: "white", marginTop: "16px", boxSizing: "border-box", fontSize: "1rem" },
        button: { width: "100%", backgroundColor: "#4f46e5", color: "white", border: "none", padding: "14px", borderRadius: "8px", marginTop: "24px", fontWeight: "bold", cursor: "pointer", fontSize: "1rem", letterSpacing: "1px" },
        logoutBtn: { backgroundColor: "transparent", color: "#94a3b8", border: "1px solid #475569", padding: "8px 16px", borderRadius: "8px", cursor: "pointer", fontWeight: "bold", fontSize: "0.875rem" },
        log: { backgroundColor: "#0f172a", padding: "16px", borderRadius: "8px", border: "1px solid #334155", borderLeft: "4px solid #4f46e5", marginBottom: "12px", fontFamily: "monospace", fontSize: "0.9rem", color: "#cbd5e1" },
        emptyLog: { textAlign: "center", padding: "40px", color: "#64748b", border: "1px dashed #334155", borderRadius: "8px", fontStyle: "italic" }
    };

    // --- UI: LOGIN SCREEN ---
    if (!isLoggedIn) {
        return (
            <div style={styles.loginContainer}>
                <div style={styles.loginCard}>
                    <h2 style={{ ...styles.title, marginBottom: "8px" }}>System Login</h2>
                    <p style={{ color: "#94a3b8", marginBottom: "32px", fontSize: "0.9rem" }}>Authorized Personnel Only</p>
                    <form onSubmit={handleLogin}>
                        <input style={styles.input} type="text" placeholder="Username (admin)" value={loginUsername} onChange={e => setLoginUsername(e.target.value)} />
                        <input style={styles.input} type="password" placeholder="Password (password123)" value={loginPassword} onChange={e => setLoginPassword(e.target.value)} />
                        <button style={styles.button} type="submit">AUTHENTICATE</button>
                    </form>
                </div>
            </div>
        );
    }

    // --- UI: DASHBOARD SCREEN ---
    return (
        <div style={styles.container}>
            <div style={styles.header}>
                <div>
                    <h1 style={styles.title}>EventStream Monitor</h1>
                    <div style={styles.badge}>{isConnected ? "SYSTEM ACTIVE" : "DISCONNECTED"}</div>
                </div>
                <button style={styles.logoutBtn} onClick={() => setIsLoggedIn(false)}>Sign Out</button>
            </div>

            <div style={styles.grid}>
                <div style={styles.card}>
                    <h3 style={{ margin: "0 0 8px 0", color: "#f8fafc" }}>Dispatch Order</h3>
                    <p style={{ color: "#94a3b8", fontSize: "0.875rem", margin: "0 0 16px 0" }}>Send an event to the RabbitMQ broker.</p>
                    <form onSubmit={sendNotification}>
            <textarea
                style={{ ...styles.input, minHeight: "120px", resize: "vertical" }}
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder="Enter order name..."
            />
                        <button style={styles.button} type="submit" disabled={!isConnected}>PUSH TO BROKER</button>
                    </form>
                </div>

                <div>
                    <h3 style={{ margin: "0 0 24px 0", color: "#f8fafc" }}>Live Telemetry Logs</h3>
                    {notifications.length === 0 ? (
                        <div style={styles.emptyLog}>System idle. Waiting for events...</div>
                    ) : (
                        notifications.map((n, i) => (
                            <div key={i} style={styles.log}>
                                <span style={{ color: "#34d399", marginRight: "8px" }}>[EVENT]</span>
                                {n}
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}