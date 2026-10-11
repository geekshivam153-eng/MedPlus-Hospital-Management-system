import React from 'react';
import { createRoot } from 'react-dom/client';
import RegistrationForm from '../components/RegistrationForm.jsx';
import HospitalDashboard from '../components/HospitalDashboard.jsx';
import './styles.css';

function App() {
  const [refreshKey, setRefreshKey] = React.useState(0);

  return (
    <main className="app-shell">
      <header className="hero">
        <div className="hero-copy">
          <span className="eyebrow"><span className="status-dot" /> HOSPITAL CARE, CONNECTED</span>
          <h1>Care that feels<br /><span>closer.</span></h1>
          <p>A calmer, clearer way to manage patient admissions and care.</p>
        </div>
        <div className="hero-art" aria-hidden="true">
          <div className="hero-orbit orbit-one" />
          <div className="hero-orbit orbit-two" />
          <div className="medical-cross"><span /><span /></div>
          <div className="hero-art-caption"><span>✦</span> Here for every heartbeat</div>
        </div>
        <div className="hero-bottom">
          <span><span className="hero-bottom-icon">✚</span> Compassionate care starts here</span>
          <span className="hero-bottom-right">PATIENT SERVICES <span>·</span> 24/7</span>
        </div>
      </header>

      <div className="content-wrap">
        <section className="section-heading">
          <div>
            <span className="eyebrow">WELCOME TO YOUR CARE DESK</span>
            <h2>Good care starts with a warm welcome.</h2>
          </div>
          <span className="secure-badge"><span>✓</span> Secure patient registration</span>
        </section>
        <RegistrationForm onPatientRegistered={() => setRefreshKey(key => key + 1)} />
        <HospitalDashboard key={refreshKey} />
        <footer className="page-footer">
          <span>✚ &nbsp;Care, with clarity.</span>
          <span>Patient information is handled with care.</span>
        </footer>
      </div>
    </main>
  );
}

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
