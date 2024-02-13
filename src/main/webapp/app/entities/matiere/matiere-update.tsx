import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISemestre } from 'app/shared/model/semestre.model';
import { getEntities as getSemestres } from 'app/entities/semestre/semestre.reducer';
import { INote } from 'app/shared/model/note.model';
import { getEntities as getNotes } from 'app/entities/note/note.reducer';
import { ISeance } from 'app/shared/model/seance.model';
import { getEntities as getSeances } from 'app/entities/seance/seance.reducer';
import { IProfesseur } from 'app/shared/model/professeur.model';
import { getEntities as getProfesseurs } from 'app/entities/professeur/professeur.reducer';
import { IMatiere } from 'app/shared/model/matiere.model';
import { getEntity, updateEntity, createEntity, reset } from './matiere.reducer';

export const MatiereUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const semestres = useAppSelector(state => state.semestre.entities);
  const notes = useAppSelector(state => state.note.entities);
  const seances = useAppSelector(state => state.seance.entities);
  const professeurs = useAppSelector(state => state.professeur.entities);
  const matiereEntity = useAppSelector(state => state.matiere.entity);
  const loading = useAppSelector(state => state.matiere.loading);
  const updating = useAppSelector(state => state.matiere.updating);
  const updateSuccess = useAppSelector(state => state.matiere.updateSuccess);

  const handleClose = () => {
    navigate('/matiere');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSemestres({}));
    dispatch(getNotes({}));
    dispatch(getSeances({}));
    dispatch(getProfesseurs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...matiereEntity,
      ...values,
      semestre: semestres.find(it => it.id.toString() === values.semestre.toString()),
      note: notes.find(it => it.id.toString() === values.note.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...matiereEntity,
          semestre: matiereEntity?.semestre?.id,
          note: matiereEntity?.note?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.matiere.home.createOrEditLabel" data-cy="MatiereCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.matiere.home.createOrEditLabel">Create or edit a Matiere</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="matiere-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.matiere.nomMatiere')}
                id="matiere-nomMatiere"
                name="nomMatiere"
                data-cy="nomMatiere"
                type="text"
              />
              <ValidatedField
                id="matiere-semestre"
                name="semestre"
                data-cy="semestre"
                label={translate('isimmManagerApp.matiere.semestre')}
                type="select"
              >
                <option value="" key="0" />
                {semestres
                  ? semestres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="matiere-note" name="note" data-cy="note" label={translate('isimmManagerApp.matiere.note')} type="select">
                <option value="" key="0" />
                {notes
                  ? notes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/matiere" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MatiereUpdate;
