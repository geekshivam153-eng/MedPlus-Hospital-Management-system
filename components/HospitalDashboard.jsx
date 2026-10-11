import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function HospitalDashboard() {
    const [activeAdmissions, setActiveAdmissions] = useState([]);
    const [dischargedAdmissions, setDischargedAdmissions] = useState([]);
    const [selectedAdmission, setSelectedAdmission] = useState(null);
    const [newItem, setNewItem] = useState({ itemCategory: 'MEDICINE', description: '', amount: '' });

    const fetchData = async () => {
        const active = await axios.get('http://localhost:8080/api/patients/active');
        const discharged = await axios.get('http://localhost:8080/api/patients/discharged');
        setActiveAdmissions(active.data);
        setDischargedAdmissions(discharged.data);
    };

    useEffect(() => { fetchData(); }, []);

    const handleAddItem = async (admissionId) => {
        await axios.post(`http://localhost:8080/api/admissions/${admissionId}/items`, newItem);
        setNewItem({ itemCategory: 'MEDICINE', description: '', amount: '' });
        fetchData();
    };

    const handleDeleteItem = async (admissionId, itemId) => {
        await axios.delete(`http://localhost:8080/api/admissions/${admissionId}/items/${itemId}`);
        fetchData();
    };

    const handleFinalizeAndDischarge = async (admissionId) => {
        const res = await axios.post(`http://localhost:8080/api/admissions/${admissionId}/discharge`);
        alert(`Discharge complete! Total Paid: ₹${res.data.netPayable}`);
        setSelectedAdmission(null);
        fetchData();
    };

    return (
        <div className="dashboard-layout">
            <div className="admissions-column">
                <section className="dashboard-section">
                    <div className="section-title-row">
                        <div>
                            <span className="eyebrow">LIVE OVERVIEW</span>
                            <h2>Patient care</h2>
                        </div>
                        <span className="count-badge">{activeAdmissions.length} active</span>
                    </div>
                    <h3 className="list-heading"><span className="live-dot" /> Active in-patients <span>{activeAdmissions.length}</span></h3>
                    {activeAdmissions.length === 0 && <div className="empty-state"><span className="empty-icon">♡</span><strong>All clear for now</strong><p>New admissions will appear here.</p></div>}
                    <div className="admission-list">
                        {activeAdmissions.map(adm => (
                            <article className="patient-card" key={adm.id}>
                                <div className="patient-avatar">{adm.patient.fullName.charAt(0)}</div>
                                <div className="patient-info">
                                    <strong>{adm.patient.fullName}</strong>
                                    <span>{adm.patient.uniqueUhid}</span>
                                    <small>{adm.doctor.name} <i>·</i> {adm.injurySeverity.toLowerCase()} priority</small>
                                </div>
                                <button className="secondary-button" onClick={() => setSelectedAdmission(adm)}>Manage bill <span>→</span></button>
                            </article>
                        ))}
                    </div>
                </section>

                <section className="dashboard-section discharged-section">
                    <div className="section-title-row compact-title">
                        <h3 className="list-heading">Recently discharged <span>{dischargedAdmissions.length}</span></h3>
                    </div>
                    {dischargedAdmissions.length === 0 && <p className="muted-empty">Discharged patients will appear here.</p>}
                    <div className="admission-list">
                        {dischargedAdmissions.map(adm => (
                            <article className="patient-card discharged-card" key={adm.id}>
                                <div className="patient-avatar discharged-avatar">✓</div>
                                <div className="patient-info">
                                    <strong>{adm.patient.fullName}</strong>
                                    <span>{adm.patient.uniqueUhid}</span>
                                </div>
                                <span className="discharged-date">{new Date(adm.dischargedAt).toLocaleDateString()}</span>
                            </article>
                        ))}
                    </div>
                </section>
            </div>

            {selectedAdmission && (
                <section className="billing-card">
                    <div className="billing-header">
                        <div className="billing-icon">✚</div>
                        <div><span className="eyebrow">PATIENT BILLING</span><h3>{selectedAdmission.patient.fullName}</h3></div>
                        <button className="close-button" aria-label="Close billing panel" onClick={() => setSelectedAdmission(null)}>×</button>
                    </div>
                    <div className="advance-summary"><span>Registration advance</span><strong>₹{selectedAdmission.registrationFee}</strong></div>
                    <div className="table-wrap">
                        <table className="billing-table">
                            <thead><tr><th>Type</th><th>Description</th><th>Amount</th><th>Action</th></tr></thead>
                            <tbody>
                                {selectedAdmission.billingItems.map(item => (
                                    <tr key={item.id}>
                                        <td><span className="item-category">{item.itemCategory.replaceAll('_', ' ')}</span></td>
                                        <td>{item.description}</td>
                                        <td className="amount-cell">₹{item.amount}</td>
                                        <td><button className="delete-button" onClick={() => handleDeleteItem(selectedAdmission.id, item.id)}>Remove</button></td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                    <div className="add-charge">
                        <h4>Add a care charge</h4>
                        <div className="charge-fields">
                            <select aria-label="Charge category" value={newItem.itemCategory} onChange={e => setNewItem({...newItem, itemCategory: e.target.value})}>
                                <option value="MEDICINE">Medicine</option>
                                <option value="SCAN_MRI">MRI Scan</option>
                                <option value="SCAN_CT">CT Scan</option>
                                <option value="XRAY">X-Ray</option>
                                <option value="DOCTOR_FEE">Doctor Fee</option>
                                <option value="HOSPITAL_CHARGE">Hospital Charge</option>
                            </select>
                            <input aria-label="Charge description" type="text" placeholder="Description" value={newItem.description} onChange={e => setNewItem({...newItem, description: e.target.value})} />
                            <input aria-label="Charge amount" type="number" placeholder="Amount (₹)" value={newItem.amount} onChange={e => setNewItem({...newItem, amount: e.target.value})} />
                            <button className="secondary-button add-button" onClick={() => handleAddItem(selectedAdmission.id)}>Add item <span>+</span></button>
                        </div>
                    </div>
                    <button className="discharge-button" onClick={() => handleFinalizeAndDischarge(selectedAdmission.id)}>
                        Generate invoice & discharge <span>→</span>
                    </button>
                </section>
            )}
        </div>
    );
}