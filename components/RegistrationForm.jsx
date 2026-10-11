import React, { useState } from 'react';
import axios from 'axios';

export default function RegistrationForm({ onPatientRegistered }) {
    const [formData, setFormData] = useState({
        name: '',
        age: '',
        place: '',
        idCardType: 'AADHAAR',
        doctorId: '1',
        severity: 'MINOR'
    });
    const [file, setFile] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const data = new FormData();
        Object.keys(formData).forEach(k => data.append(k, formData[k]));
        data.append('document', file);

        try {
            const res = await axios.post('http://localhost:8080/api/patients/register', data);
            alert(`Patient Registered! Generated UHID: ${res.data.patient.uniqueUhid}`);
            onPatientRegistered();
        } catch (err) {
            console.error(err);
            alert('Registration failed.');
        }
    };

    return (
        <form className="registration-card" onSubmit={handleSubmit}>
            <div className="card-heading">
                <div className="heading-icon">✚</div>
                <div>
                    <span className="eyebrow">NEW PATIENT</span>
                    <h3>Patient registration</h3>
                    <p>Let’s get the essentials so we can take great care of you.</p>
                </div>
                <span className="fee-badge"><span>Registration</span><strong>₹1,500</strong></span>
            </div>

            <div className="form-grid">
                <label className="field field-wide">
                    <span>Full name</span>
                    <input type="text" placeholder="e.g. Aisha Sharma" required onChange={e => setFormData({...formData, name: e.target.value})} />
                </label>
                <label className="field">
                    <span>Age</span>
                    <input type="number" placeholder="Enter age" required onChange={e => setFormData({...formData, age: e.target.value})} />
                </label>
                <label className="field">
                    <span>City / place</span>
                    <input type="text" placeholder="Where are you from?" required onChange={e => setFormData({...formData, place: e.target.value})} />
                </label>
                <label className="field">
                    <span>Identity document</span>
                    <select onChange={e => setFormData({...formData, idCardType: e.target.value})}>
                        <option value="AADHAAR">Aadhaar Card</option>
                        <option value="PAN">PAN Card</option>
                    </select>
                </label>
                <label className="field">
                    <span>Care priority</span>
                    <select onChange={e => setFormData({...formData, severity: e.target.value})}>
                        <option value="MINOR">Minor injury</option>
                        <option value="MODERATE">Moderate injury</option>
                        <option value="MAJOR">Major injury</option>
                    </select>
                </label>
                <label className="field field-wide upload-field">
                    <span>Upload identity document</span>
                    <input type="file" required onChange={e => setFile(e.target.files[0])} />
                    <small>Accepted document: Aadhaar or PAN card</small>
                </label>
            </div>
            <div className="form-actions">
                <span className="form-note"><span>✓</span> Your details are kept private and secure</span>
                <button className="primary-button" type="submit">Complete registration <span>→</span></button>
            </div>
        </form>
    );
}